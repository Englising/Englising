package org.englising.com.englisingbe.multiplay.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MultiPlaySentence {
    private int sentenceIndex;
    private BigDecimal startTime;
    private BigDecimal endTime;
    private List<MultiPlayWord> alphabets;
}
