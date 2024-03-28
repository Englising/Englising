package org.englising.com.englisingbe.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.englising.com.englisingbe.auth.SecurityAllowedUrls;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.englising.com.englisingbe.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

// Filter를 적용함으로써 servlet에 도달하기 전에 검증 완료 가능
// JwtProvider가 검증을 끝낸 Jwt로부터 유저 정보를 조회해와서
// UserPasswordAuthenticationFilter로 전달
@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;

    // 토큰 유효한지 확인 후 SecurityContext에 계정정보 저장
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {

        AntPathMatcher pathMatcher = new AntPathMatcher();
        boolean skipFilter = Arrays.stream(SecurityAllowedUrls.NO_CHECK_URL)
                .anyMatch(url -> pathMatcher.match(url, request.getRequestURI()));

        if (skipFilter) {
            filterChain.doFilter(request, response);
            return;
        }

        // 쿠키에서 accessToken 추출.
        String accessToken = cookieUtil.getAccessTokenFromCookie(request);

        //accessToken이 null이거나 유효하지 않으면 refreshToken 확인
        if (accessToken == null || !jwtProvider.isTokenValid(accessToken)) {
            String refreshToken = cookieUtil.getRefreshTokenFromCookie(request);

            // refreshToken null이거나 만료시 다음 필터로 넘기지 않고 에러 반환
            if (refreshToken == null || !jwtProvider.isTokenValid(refreshToken)) {

                response.setContentType("application/json");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(response.getWriter(), ErrorHttpStatus.UNAUTHORIZED_REFRESH_TOKEN);
                return;

            } else {
                // refresh token이 유효한 경우
                try {
                    Long userId = jwtProvider.getUserId(refreshToken).orElse(null);
                    JwtResponseDto jwtResponseDto = jwtProvider.createTokens(jwtProvider.getAuthentication(refreshToken), userId);

//                    Cookie accessCookie = cookieUtil.createAccessCookie("Authorization", jwtResponseDto.getAccessToken());
//                    Cookie refreshCookie = cookieUtil.createRefreshCookie("Authorization-refresh", jwtResponseDto.getRefreshToken());
//                    response.addCookie(accessCookie);
//                    response.addCookie(refreshCookie);

                    cookieUtil.addAccessCookie(response, "Authorization", jwtResponseDto.getAccessToken());
                    cookieUtil.addRefreshCookie(response, "Authorization-refresh", jwtResponseDto.getRefreshToken());

                    response.setContentType("application/json");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());

                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.writeValue(response.getWriter(), ErrorHttpStatus.UNAUTHORIZED_ACCESS_TOKEN);
                    return;

                } catch (Exception e) {
                    log.info("JwtAuthenticationFilter -> " + e.getMessage());

                    PrintWriter writer = response.getWriter();
                    writer.print("Error while refreshing token: {}" + e.getMessage());

                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
        }

        // accessToken이 null이 아니고 유효하다면 인증 진행
        //유효한 토큰이면 해당 토큰으로 Authentication 가져와서 SecurityContext에 저장
        Authentication authentication = jwtProvider.getAuthentication(accessToken); // 스프링 시큐리티 인증 토큰 생성
        SecurityContextHolder.getContext().setAuthentication(authentication); //세션에 사용자 등록
        filterChain.doFilter(request, response);
    }
}



