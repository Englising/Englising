package org.englising.com.englisingbe.multiplay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultiplayRoomStartRequest {
    private Long multiplayId;
    private Long trackId;
    private String roomName;
    private int totalPeople;
    private String genre;
    private boolean isSecret;
    private int roomPw;
}