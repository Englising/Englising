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
public class SinglePlayResponseDto {
    private Long singlePlayId;
    private List<LyricDto> lyrics;
    private List<WordResponseDto> words;
    private int totalWordCnt;
    private int rightWordCnt;
}
