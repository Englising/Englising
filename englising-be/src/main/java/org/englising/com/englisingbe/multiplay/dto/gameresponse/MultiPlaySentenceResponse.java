package org.englising.com.englisingbe.multiplay.dto.gameresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MultiPlaySentenceResponse {
    private Float startTime;
    private Float endTime;
    private List<MultiPlayWordResponse> alphabets;
}
