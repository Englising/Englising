package org.englising.com.englisingbe.singleplay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordResponseDto {
    public Long singleplayWordId;
    public Integer sentenceIndex; // 문장의 번호
    public Integer wordIndex; // 문장 내 단어 번호
    public String word;
    public Boolean isRight;
}
