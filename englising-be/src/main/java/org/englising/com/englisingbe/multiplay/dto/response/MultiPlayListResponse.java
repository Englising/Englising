package org.englising.com.englisingbe.multiplay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultiPlayListResponse {
    public Long multiplayId;
    public String roomName;
    public int totalPeople;
    public String multiplayImg;
}
