package org.englising.com.englisingbe.jwt;

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

    // 쿠키 생성
    public Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    // 쿠키에서 accessToken 추출
    public String getAccessTokenFromCookie (HttpServletRequest request) {
        String accessToken = null;

        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies) {
            System.out.println(cookie.getName());
            if(cookie.getName().equals("Authorization")) {
                accessToken = cookie.getValue();
            }
        }
        return accessToken;
    }


}
