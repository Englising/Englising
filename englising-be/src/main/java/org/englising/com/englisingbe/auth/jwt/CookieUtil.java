package org.englising.com.englisingbe.auth.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.springframework.http.ResponseCookie;
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
        cookie.setSecure(false); // todo. true로 변경
//        cookie.setSecure(true);
//        cookie.setDomain(".localhost");
        cookie.setHttpOnly(true);

        return cookie;
    }

    // sameSite"None"설정을 위한 cookie ... 개발용
    public void addAccessCookie(HttpServletResponse response, String key, String value) {
        ResponseCookie cookie = ResponseCookie.from(key, value)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .maxAge(3600)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void addRefreshCookie(HttpServletResponse response, String key, String value) {
        ResponseCookie cookie = ResponseCookie.from(key, value)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .maxAge(1209600)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }


    // refreshToken 쿠키 생성
    public Cookie createRefreshCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(1209600);
        cookie.setPath("/");
        cookie.setSecure(false);
//        cookie.setDomain(".localhost"); //todo. 삭제
//        cookie.setSecure(true);
        cookie.setHttpOnly(true);

        return cookie;
    }



    // 쿠키에서 accessToken 추출
    public String getAccessTokenFromCookie (HttpServletRequest request) {
        String accessToken = null;

        Cookie[] cookies = request.getCookies();
//        if(cookies == null) {
//            throw new GlobalException(ErrorHttpStatus.COOKIE_NOT_FOUNDED);
//        }

        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("Authorization")) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }

//        if(accessToken == null) {
//            throw new GlobalException(ErrorHttpStatus.UNAUTHORIZED_TOKEN);
//        }
        return accessToken;
    }

    public String getRefreshTokenFromCookie (HttpServletRequest request) {
        String refreshToken = null;

        Cookie[] cookies = request.getCookies();
//        if(cookies == null) {
//            throw new GlobalException(ErrorHttpStatus.COOKIE_NOT_FOUNDED);
//        }

        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("Authorization-refresh")) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

//        if(refreshToken == null) {
//            throw new GlobalException(ErrorHttpStatus.UNAUTHORIZED_TOKEN);
//        }
        return refreshToken;
    }


}
