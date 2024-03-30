package org.englising.com.englisingbe.multiplay.dto.socket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChatResponseDto {
    private Long userId;
    private String message;
    private boolean isMine;

    public ChatResponseDto(ChatDto chatDto,Long userId, boolean isMine){
        this.userId = userId;
        this.message = chatDto.getMessage();
        this.isMine = isMine;
    }
}
