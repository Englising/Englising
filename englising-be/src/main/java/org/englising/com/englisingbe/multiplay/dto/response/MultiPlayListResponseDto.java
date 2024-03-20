package org.englising.com.englisingbe.multiplay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultiPlayListResponseDto {
    private Long multiplayId;
    private String roomName;
    private Integer currentUser;
    private Integer maxUser;
    private String multiImg;
}
