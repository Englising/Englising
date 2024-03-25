package org.englising.com.englisingbe.singleplay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LyricDto {
    public Boolean isBlank;
    public Float startTime;
    public Float endTime;
    public List<String> lyric; // 가사 단어 단위로 잘라서 주기
}
