package org.englising.com.englisingbe.multiplay.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultiPlayStartInfo {
    private String trackTitle;
    private String youtubeId;
    private int selectedHint;
    private String beforeLyric;
    private BigDecimal beforeLyricStartTime;
    private String afterLyric;
    private BigDecimal afterLyricEndTime;
    private List<MultiPlaySentence> sentences;
}
