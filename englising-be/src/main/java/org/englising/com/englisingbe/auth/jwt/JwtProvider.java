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

    private final UserRepository userRepository;
    private final CustomUserDetailService customUserDetailService;

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
                    .get("userId").toString();
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

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean isTokenValid(String token) {
        Jws<Claims> claims =
                Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }

}
