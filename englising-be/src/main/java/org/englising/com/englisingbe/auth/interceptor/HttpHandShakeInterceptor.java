package org.englising.com.englisingbe.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import org.englising.com.englisingbe.auth.jwt.CookieUtil;
import org.englising.com.englisingbe.auth.jwt.JwtProvider;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

// Websocket 최초 연결 시 HTTP 세션에서 인증 정보를 가져와 세션에 연동
public class HttpHandShakeInterceptor extends HttpSessionHandshakeInterceptor {

    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;

    public HttpHandShakeInterceptor(JwtProvider jwtProvider, CookieUtil cookieUtil) {
        this.jwtProvider = jwtProvider;
        this.cookieUtil = cookieUtil;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpServletRequest httpRequest = servletRequest.getServletRequest();
            String accessToken = cookieUtil.getAccessTokenFromCookie(httpRequest);

            // 인증이 성공적일 경우, Websocket Session의 속성에 추가
            if (accessToken != null && jwtProvider.isTokenValid(accessToken)) {
                Long userId = jwtProvider.getUserId(accessToken)
                        .orElseThrow(() -> new GlobalException(ErrorHttpStatus.USER_NOT_FOUND));
                attributes.put("userId", userId);
                System.out.println("웹소켓 핸드 쉐이크 인터셉터 : "+userId.toString()+"추가됨");
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
