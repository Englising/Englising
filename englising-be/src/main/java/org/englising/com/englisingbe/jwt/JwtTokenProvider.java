package org.englising.com.englisingbe.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.user.service.UserDetailService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.Base64;
import java.util.Date;

// 토큰 생성하고 검증하는 클래스
// 해당 컴포넌트는 Filter 클래스에서 사전 검증 거침
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.access.expiration")
    private Long accessTokenExpiration;
    @Value("${jwt.refresh.expiration")
    private Long refreshTokenExpiration;
    @Value("${jwt.access.header}")
    private String accessHeader;
    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private final UserDetailService userDetailService;

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

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Jwt 토큰에서 인증 정보 조회









}
