package org.englising.com.englisingbe.auth.jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.englising.com.englisingbe.auth.dto.CustomUserDetails;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.englising.com.englisingbe.user.repository.UserRepository;
import org.englising.com.englisingbe.auth.service.CustomUserDetailService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.stream.Collectors;

// 토큰 생성하고 검증하는 서비스
// 해당 컴포넌트는 Filter 클래스에서 사전 검증 거침
// <JwtTokenProvider>
@Component
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtProvider {

    private static final String AUTHORITIES_KEY = "auth";

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.access.expiration}")
    private Long accessTokenExpiration;
    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpiration;
    @Value("{jwt.access.header}")
    private String accessHeader;
    @Value("{jwt.refresh.header}")
    private String refreshHeader;

//    private final String header = "Authorization";

    private final UserRepository userRepository;
    private final CustomUserDetailService customUserDetailService;

    // secretKey 초기화
    // secretKey는 토큰 생성하거나 토큰 파싱할 때 사용되는 정보로 보안에 유의
    // git에 secretkey 올라가지 않도록 application-dev.yml 파일에 secret값 분리
//    @PostConstruct
//    protected void init() {
//        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
//    }

    /**
     * JWT의 헤더에 들어오는 값 : 'Authorization(Key) = Bearer {토큰}'
     * JWT의 Subject로 userId 사용
     * 
     * */

    // 권한 정보 획득 (Spring Security 인증과정에서 권한 확인을 위한 기능)
    //jwt 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

        if(claims.get(AUTHORITIES_KEY) == null) {
            throw new GlobalException(ErrorHttpStatus.UNAUTHORIZED_TOKEN);
        }

        // UserDetails 객체 만들고 Authentication 반환
        CustomUserDetails customUserDetails =  (CustomUserDetails) customUserDetailService.loadUserByUsername(claims.get("userId").toString());
        return new UsernamePasswordAuthenticationToken(customUserDetails, customUserDetails.getPassword(), customUserDetails.getAuthorities());
    }

    // 토큰에서 userId 추출 (토큰 파싱)
    public Optional<Long> getUserId(String token) {
        try{
            String userId = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return Optional.of(Long.parseLong(userId));
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }
    
    // Accesstoken, RefreshToken 발급
    public JwtResponseDto createTokens(Authentication authentication, Long userId) {
        String accessToken = createAccessToken(authentication, userId);
        String refreshToken = createRefreshToken(authentication, userId);
        
        return new JwtResponseDto(userId, accessToken, refreshToken);
    }


    // AccessToken 생성
    public String createAccessToken(Authentication authentication, Long userId) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();

        // JWT payload에 저장되는 정보단위
        Claims claims = Jwts.claims()
                .setSubject(authentication.getName())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenExpiration));

        claims.put("userId", userId); // userId 넣어주기
        claims.put(AUTHORITIES_KEY, authorities); // ROLE_USER 권한

        return Jwts.builder()
                .setClaims(claims) //정보 저장
                .signWith(SignatureAlgorithm.HS256, secretKey) //사용할 알고리즘, signature에 들어갈 secretKey
                .compact();
    }

    // RefreshToken 생성
    public String createRefreshToken(Authentication authentication, Long userId) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();

        Claims claims = Jwts.claims()
                .setSubject(authentication.getName())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenExpiration));

        claims.put("userId", userId);
        claims.put(AUTHORITIES_KEY, authorities);
//        claims.put("token_type", "refresh_token"); // refreshToken 타입 저장

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * 헤더에서 순수 Token 추출
     */
    public Optional<String> extractAccessTokenFromHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(token -> token.startsWith("Bearer "))
                .map(token -> token.replace("Bearer ", ""));
    }

    public Optional<String> extractRefreshTokenFromHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(token -> token.startsWith("Bearer "))
                .map(token -> token.replace("Bearer ", ""));
    }

    // 토큰 유효성 검사 + 만료일자 확인
    public boolean isTokenValid(String token) {
        try{
            Jws<Claims> claims =
                    Jwts.parserBuilder()
                            .setSigningKey(secretKey)
                            .build()
                            .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    // AccessToken인지 RefreshToken인지 구분하여 반환하는 메서드
//    public JwtType getTokenTypeFromHeader(String token) {
//        if(isTokenValid(token)) {
//            Jws<Claims> claims = Jwts.parserBuilder()
//                    .setSigningKey(secretKey)
//                    .build()
//                    .parseClaimsJws(token);
//            if(claims.getBody().get("token_type").equals("refresh_token")) {
//                return JwtType.REFRESH_TOKEN;
//            } else {
//                return JwtType.ACCESS_TOKEN;
//            }
//        }
//        return JwtType.INVALID_TOKEN;
//    };

    // AccessToken + RefreshToken 헤더에 넣기
    public void setAccessAndRefreshToken (HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
        response.setHeader(refreshHeader, refreshToken);
        log.info("Access Token, Refresh Token 헤더 설정 완료", accessToken, refreshToken);
    }

    // AccessToken 헤더에 넣기
    public void setAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
        log.info("AccessToken 헤더 설정 완료 ", accessToken);
    }

//      RefreshToken DB 저장 (업데이트) -> todo. Redis로 수정
//    public void updateRefreshToken(String email, String refreshToken) {
//        userRepository.findByEmail(email)
//                .ifPresentOrElse(
//                        user -> user.updateRefreshToken(refreshToken),
//                        () -> new Exception("일치하는 회원이 없습니다.")
//                );
//    }

}
