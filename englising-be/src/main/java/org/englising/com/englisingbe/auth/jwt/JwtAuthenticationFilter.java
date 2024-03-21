package org.englising.com.englisingbe.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.englising.com.englisingbe.auth.AllowedUrls;
import org.englising.com.englisingbe.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

// Filter를 적용함으로써 servlet에 도달하기 전에 검증 완료 가능
// JwtProvider가 검증을 끝낸 Jwt로부터 유저 정보를 조회해와서
// UserPasswordAuthenticationFilter로 전달
@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // todo. "kakao로그인 페이지 get 요청" 추가

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final CookieUtil cookieUtil;

    // 토큰 유효한지 확인 후 SecurityContext에 계정정보 저장하는 메소드
    /**
     * 1. 쿠키에서 JWT 받아온다
     * 2. 유효한 토큰인지 확인한다
     * 3. 토큰이 유효하면 토큰으로부터 유저 정보 받아온다
     * 4. SecurityContext에 Authentication 객체 저장한다.
     * */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        // 로그인 요청은 필터 진행 안하고 넘어감
//        boolean skipFilter = Arrays.stream(NO_CHECK_URL).anyMatch(url -> request.getRequestURI().equals(url));
//        if (skipFilter) {
//            filterChain.doFilter(request, response);
//            return;
//        }

        AntPathMatcher pathMatcher = new AntPathMatcher();
        boolean skipFilter = Arrays.stream(AllowedUrls.NO_CHECK_URL).anyMatch(url -> pathMatcher.match(url, request.getRequestURI()));

        if (skipFilter) {
            filterChain.doFilter(request, response);
            return;
        }

        // 쿠키에서 accessToken 추출.
        String accessToken = cookieUtil.getAccessTokenFromCookie(request);

        try {
            //  2 3 4
            if (accessToken != null && jwtProvider.isTokenValid(accessToken)) {    // accessToken이 유효한 경우
                //유효한 토큰이면 해당 토큰으로 Authentication 가져와서 SecurityContext에 저장
                Authentication authentication = jwtProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("인증 성공");
            } else { // accessToken이 유효하지 않은 경우
                System.out.println("accessToken이 유효하지 않습니다. ");
                System.out.println("refreshToken 꺼내서 확인 후 유효성 확인 시작. ");

                String refreshToken = cookieUtil.getRefreshTokenFromCookie(request);
                if(refreshToken != null && jwtProvider.isTokenValid(refreshToken)) { // refreshToken 유효한 경우
                    // refreshToken이 유효하면 둘다 재발급하고 쿠키추가
                    Long userId = jwtProvider.getUserId(refreshToken).orElse(null);
                    JwtResponseDto jwtResponseDto =
                            jwtProvider.createTokens(jwtProvider.getAuthentication(refreshToken), userId);

                    Cookie accessCookie = cookieUtil.createAccessCookie("Authorization", jwtResponseDto.getAccessToken());
                    Cookie refreshCookie = cookieUtil.createRefreshCookie("Authorization-refresh", jwtResponseDto.getRefreshToken());

                    response.addCookie(accessCookie);
                    response.addCookie(refreshCookie);
                    response.sendRedirect("http://localhost:8080/"); //todo. 프론트측 특정 url ex:localhost:3030 넣기 (로그인 후 리다이렉트될)

                    return;
                } else { // refreshToken 유효하지 않은 경우
                    // todo. 에러 리턴
                    return;
                }
            }
        } catch (Exception e) {
            request.setAttribute("exeption", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}


