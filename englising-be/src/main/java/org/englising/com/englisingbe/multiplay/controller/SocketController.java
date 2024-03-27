package org.englising.com.englisingbe.multiplay.controller;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.multiplay.dto.socket.AnswerDto;
import org.englising.com.englisingbe.multiplay.service.AnswerInputService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.englising.com.englisingbe.multiplay.dto.socket.ChatDto;

@RestController
@RequiredArgsConstructor
public class SocketController {
    private final AnswerInputService answerInputService;

    @MessageMapping("/chat/{multiPlayId}")
    @SendTo("/sub/chat/{multiPlayId}")
    public ChatDto sendChat(@DestinationVariable Long multiPlayId, @RequestBody ChatDto chatDto){
        //TODO 본인인지 확인하는 로직(인증 방법 찾아보기), 실제 참여자인지 확인하는 로직
        return chatDto;
    }

    @MessageMapping("/answer/{multiPlayId}")
    @SendTo("/sub/answer/{multiPlayId}")
    public AnswerDto sendAnswer(@DestinationVariable Long multiPlayId, @RequestBody AnswerDto answerDto){
        answerInputService.updateAnswer(multiPlayId, answerDto, 1L); //TODO userId 쿠키에서 가져오기
        return answerDto;
    }
}
