package org.englising.com.englisingbe.multiplay.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultiPlayStartInfo {
    private String trackTitle;
    private String youtubeId;
    private String beforeLyric;
    private String afterLyric;
    private List<MultiPlaySentence> sentences;
}
