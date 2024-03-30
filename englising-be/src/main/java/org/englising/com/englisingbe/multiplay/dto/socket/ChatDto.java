package org.englising.com.englisingbe.multiplay.dto.socket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChatDto {
    private Long userId;
    private String message;
}
