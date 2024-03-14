package org.englising.com.englisingbe.singleplay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartResponseDto {
    public Long singleplayId;
    public List<LyricDto> lyrics;
    public List<WordResponseDto> words;
    public Integer totalWordCnt;
    public Integer rightWordCnt;
}
