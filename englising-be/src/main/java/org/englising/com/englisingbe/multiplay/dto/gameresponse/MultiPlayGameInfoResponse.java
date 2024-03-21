package org.englising.com.englisingbe.multiplay.dto.gameresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MultiPlayGameInfoResponse {
    private Long trackId;
    private String youtubeId;
    private Integer leftTime;
    private List<MultiPlaySentenceResponse> sentences;
}
