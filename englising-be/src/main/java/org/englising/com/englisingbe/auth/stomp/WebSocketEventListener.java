package org.englising.com.englisingbe.auth.stomp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
    private final SubscriptionService subscriptionService;

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String topic = accessor.getDestination();

        // 핸드셰이크 인터셉터에서 설정된 userId 속성 가져오기
        Long userId = (Long) accessor.getSessionAttributes().get("userId");
        subscriptionService.addSubscription(topic, sessionId, userId);
        log.info("User " + userId + " added subscription to " + topic + " for session " + sessionId);
    }

    @EventListener
    public void handleSessionUnsubscribeEvent(SessionUnsubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String topic = accessor.getDestination();

        // 핸드셰이크 인터셉터에서 설정된 userId 속성 가져오기
        Long userId = (Long) accessor.getSessionAttributes().get("userId");
        subscriptionService.removeSubscription(topic, sessionId, userId);
        log.info("User " + userId + " removed subscription from " + topic + " for session " + sessionId);
    }
}
