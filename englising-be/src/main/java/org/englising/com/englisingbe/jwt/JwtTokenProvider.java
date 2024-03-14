package org.englising.com.englisingbe.jwt;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.bind.DatatypeConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.englising.com.englisingbe.user.repository.UserRepository;
import org.englising.com.englisingbe.user.service.UserDetailService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

// 토큰 생성하고 검증하는 서비스
// 해당 컴포넌트는 Filter 클래스에서 사전 검증 거침
// <JwtTokenProvider>
@Component
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.access.expiration}")
    private Long accessTokenExpiration;
    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpiration;

    private final String header = "Authorization";
//    @Value("${jwt.access.header}")
//    private String accessHeader;
//    @Value("${jwt.refresh.header}")
//    private String refreshHeader;

    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    // secretKey 초기화
    // secretKey는 토큰 생성하거나 토큰 파싱할 때 사용되는 정보로 보안에 유의
    // git에 secretkey 올라가지 않도록 application-dev.yml 파일에 secret값 분리
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // AccessToken 생성
    public String createAccessToken(String userId) {
        Claims claims = Jwts.claims().setSubject(userId); // JWT payload에 저장되는 정보단위, 이걸로 user식별 (subject)

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) //정보 저장
                .setIssuedAt(now) //토큰 발행 시간
                .setExpiration(new Date(now.getTime() + accessTokenExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey) //사용할 알고리즘, signature에 들어갈 secretKey
                .compact();
    }

    // RefreshToken 생성
    public String createRefreshToken(String userId) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("token_type", "refresh_token");

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // 토큰에서 userId 추출 (토큰 파싱)
    public Optional<String> getUserId(String token) {
        try{
            String userId = Jwts.parserBuilder()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return Optional.ofNullable(userId);
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    // AccessToken인지 RefreshToken인지 구분하여 반환하는 메서드
    public JwtTokenType getTokenTypeFromHeader(String token) {
        if(isTokenValid(token)) {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                    .build()
                    .parseClaimsJws(token);
            if(claims.getBody().get("token_type").equals("refresh_token")) {
                return JwtTokenType.REFRESH_TOKEN;
            } else {
                return JwtTokenType.ACCESS_TOKEN;
            }
        }
        return JwtTokenType.INVALID_TOKEN;
    };

    /**
     * 헤더에서 Token 추출
     * 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기
     * 헤더 가져온 후 "Bearer" 삭제 (""로 replace)
     */
    public Optional<String> extractTokenFromHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(header))
                .filter(token -> token.startsWith("Bearer "))
                .map(token -> token.replace("Bearer ", ""));
    }

//    // AccessToken + RefreshToken 헤더에 넣기
//    public void setAccessAndRefreshToken (HttpServletResponse response, String accessToken, String refreshToken) {
//        response.setStatus(HttpServletResponse.SC_OK);
//
//        response.setHeader(accessHeader, accessToken);
//        response.setHeader(refreshHeader, refreshToken);
//        log.info("Access Token, Refresh Token 헤더 설정 완료", accessToken, refreshToken);
//    }
//
//    // AccessToken 헤더에 넣기
//    public void setAccessToken(HttpServletResponse response, String accessToken) {
//        response.setStatus(HttpServletResponse.SC_OK);
//
//        response.setHeader(accessHeader, accessToken);
//        log.info("재발급된 AccessToken: ", accessToken);
//    }

    //jwt 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        String userId = getUserId(token).toString();
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰 유효성 검사 + 만료일자 확인
    public boolean isTokenValid(String token) {
        try{
            Jws<Claims> claims =
                    Jwts.parserBuilder()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
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

     // RefreshToken DB 저장 (업데이트) -> todo. Redis로 수정
//    public void updateRefreshToken(String email, String refreshToken) {
//        userRepository.findByEmail(email)
//                .ifPresentOrElse(
//                        user -> user.updateRefreshToken(refreshToken),
//                        () -> new Exception("일치하는 회원이 없습니다.")
//                );
//    }

}
