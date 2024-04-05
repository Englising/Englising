package org.englising.com.englisingbe.singleplay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SinglePlayResultResponseDto {
    private Long singlePlayId;
    private List<LyricDto> lyrics;
    private List<WordResultResponseDto> words;
    private int totalWordCnt;
    private int rightWordCnt;
}
