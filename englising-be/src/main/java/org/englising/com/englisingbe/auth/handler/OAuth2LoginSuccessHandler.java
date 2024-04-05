package org.englising.com.englisingbe.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.englising.com.englisingbe.auth.jwt.CookieUtil;
import org.englising.com.englisingbe.auth.jwt.JwtProvider;
import org.englising.com.englisingbe.auth.jwt.JwtResponseDto;
import org.englising.com.englisingbe.auth.CustomOAuth2User;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.user.entity.User;
import org.englising.com.englisingbe.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

// OAuth2 로그인 성공 시 로직 처리
// 로그인 성공했으면 accessToken, refreshToken 생성 후 헤더애 넣어주기
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final CookieUtil cookieUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("OAuth2 로그인 성공 - SuccessHandler");

        try {
            // CustomOauth2User로 캐스팅하여 인증된 사용자 정보 가져옴
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            String email = oAuth2User.getEmail();

            User user = userRepository.findByEmail(email).orElse(null);

            if (user != null) {
                // 유저 정보 기반으로 토큰 생성
                JwtResponseDto jwtResponseDto = jwtProvider.createTokens(authentication, user.getUserId());

//                Cookie accessCookie = cookieUtil.createAccessCookie("Authorization", jwtResponseDto.getAccessToken());
//                Cookie refreshCookie = cookieUtil.createRefreshCookie("Authorization-refresh", jwtResponseDto.getRefreshToken());
//                response.addCookie(accessCookie);
//                response.addCookie(refreshCookie);

                cookieUtil.addAccessCookie(response, "Authorization", jwtResponseDto.getAccessToken());
                cookieUtil.addRefreshCookie(response, "Authorization-refresh", jwtResponseDto.getRefreshToken());

                response.sendRedirect("https://j10a106.p.ssafy.io/englising/selectSingle1");
            } else {
                log.info("OAuth2LoginSuccessHandler -> oAuth2User is null");

                response.setContentType("application/json");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(response.getWriter(), ErrorHttpStatus.OAUTH2_USER_NOT_FOUND);

                response.sendRedirect("https://j10a106.p.ssafy.io");
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
