package org.englising.com.englisingbe.multiplay.controller;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.englising.com.englisingbe.auth.stomp.SubscriptionService;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.englising.com.englisingbe.multiplay.dto.socket.AnswerDto;
import org.englising.com.englisingbe.multiplay.dto.socket.ChatResponseDto;
import org.englising.com.englisingbe.multiplay.service.AnswerInputService;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.englising.com.englisingbe.multiplay.dto.socket.ChatDto;

import java.security.Principal;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final SubscriptionService subscriptionService;
    private final AnswerInputService answerInputService;

    @MessageMapping("/chat/{multiPlayId}")
    public void sendChat(@DestinationVariable Long multiPlayId, @RequestBody ChatDto chatDto,
                         SimpMessageHeaderAccessor headerAccessor) {
        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
        if (userId == null) {
            throw new GlobalException(ErrorHttpStatus.USER_NOT_FOUND);
        }
        log.info("senderId = " + userId);
        Set<SubscriptionService.UserInfo> subscribers = subscriptionService.getSubscribers("/user/sub/chat/" + multiPlayId);

        subscribers.forEach(subscriberId -> {
            log.info("subscriberId = " + subscriberId.sessionId);
            boolean isMine = subscriberId.userId.equals(userId);
            ChatResponseDto chatResponseDto = new ChatResponseDto(chatDto, subscriberId.userId, isMine);
            messagingTemplate.convertAndSendToUser(subscriberId.sessionId, "/sub/chat/"+multiPlayId, chatResponseDto, createHeaders(subscriberId.sessionId));
        });
    }

    @MessageMapping("/answer/{multiPlayId}")
    @SendTo("/sub/answer/{multiPlayId}")
    public AnswerDto sendAnswer(@DestinationVariable Long multiPlayId, @RequestBody AnswerDto answerDto){
        answerInputService.updateAnswer(multiPlayId, answerDto);
        return answerDto;
    }

    private MessageHeaders createHeaders(@Nullable String sessionId){
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        if(sessionId != null) {
            headerAccessor.setSessionId(sessionId);
        }
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}
