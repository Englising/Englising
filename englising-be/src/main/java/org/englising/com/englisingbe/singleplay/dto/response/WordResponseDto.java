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
    public Long singleplayWordId;
    public Integer sentenceIndex;
    public Integer wordIndex;
    public String word;
    public Boolean isRight;
}
