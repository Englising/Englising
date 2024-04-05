package org.englising.com.englisingbe.singleplay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordResponseDto {
    private Long singleplayWordId;
    private Integer sentenceIndex;
    private Integer wordIndex;
    private String word;
    private Boolean isRight;
}
