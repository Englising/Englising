package org.englising.com.englisingbe.multiplay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.englising.com.englisingbe.multiplay.dto.socket.ChatDto;

@RestController
@RequiredArgsConstructor
public class SocketController {
    @MessageMapping("/chat/{multiPlayId}")
    @SendTo("/sub/chat/{multiPlayId}")
    public ChatDto sendChat(@DestinationVariable Long multiPlayId, @RequestBody ChatDto chatDto){
        //TODO 본인인지 확인하는 로직
        System.out.println("message = " + chatDto);
        return chatDto;
    }

    @MessageMapping("/answer/{multiPlayId}")
    @SendTo("/sub/answer/{multiPlayId}")
    public ChatDto sendAnswer(@DestinationVariable Long multiPlayId, @RequestBody ChatDto chatDto){
        //TODO
        // Redis에 multiplayId로 된 게임이 저장되어 있는지 확인 (없으면 에러 반환)
        // 현재 게임이 입력 가능한 시점인지 확인
        // 아니라면 반환하지 않음
        System.out.println("message = " + chatDto);
        return chatDto;
    }

    //TODO
    // 사람 입장 알림
    // 사람 퇴장 알림???
}
