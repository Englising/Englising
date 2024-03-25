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
    public Long singlePlayId;
    public List<LyricDto> lyrics;
    public List<WordResponseDto> words;
    public int totalWordCnt;
    public int rightWordCnt;
}
