package org.englising.com.englisingbe.multiplay.dto.gameresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MultiPlayWordResponse {
    private Integer index;
    private String word;
}
