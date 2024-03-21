package org.englising.com.englisingbe.auth.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
@Slf4j
public class CookieUtil {

    // accessToken 쿠키 생성
    public Cookie createAccessCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    // refreshToken 쿠키 생성
    public Cookie createRefreshCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(1209600);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }



    // 쿠키에서 accessToken 추출
    public String getAccessTokenFromCookie (HttpServletRequest request) {
        String accessToken = null;

        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals("Authorization")) {
                accessToken = cookie.getValue();
            }
        }
        return accessToken;
    }

    public String getRefreshTokenFromCookie (HttpServletRequest request) {
        String refreshToken = null;

        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals("Authorization-refresh")) {
                refreshToken = cookie.getValue();
            }
        }
        return refreshToken;
    }


}
