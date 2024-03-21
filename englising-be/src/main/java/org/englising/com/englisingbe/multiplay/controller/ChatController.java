package org.englising.com.englisingbe.multiplay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    @MessageMapping("/chat/1")
    @SendTo("/sub/chat/1")
    public String sendMessage(@RequestBody String message){
        System.out.println("test");
        return message;
    }
}
