package org.englising.com.englisingbe.multiplay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultiPlayUserResponseDto {
    private Long userId;
    private String nickname;
    private String profileImg;
    private String color;
    private Boolean isMe;
}
