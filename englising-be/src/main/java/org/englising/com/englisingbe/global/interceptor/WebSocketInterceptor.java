package org.englising.com.englisingbe.global.interceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.List;
import java.util.Map;

public class WebSocketInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        // HTTP 헤더에서 쿠키 추출
        Map<String, List<String>> headers = accessor.toNativeHeaderMap();
        List<String> cookies = headers.get("cookie");
        if (cookies != null) {
            // 쿠키 리스트에서 필요한 쿠키 정보를 파싱
            for (String cookie : cookies) {
                System.out.println("Cookie: " + cookie);
                // 여기에서 쿠키 값을 파싱하고 필요한 작업 수행
            }
        }

        return message;
    }
}