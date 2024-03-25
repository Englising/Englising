package org.englising.com.englisingbe.singleplay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordCheckResponseDto {
    public WordCheckResponseDto word;
    public Integer totalWordCnt;
    public Integer rightWordCnt;
}
