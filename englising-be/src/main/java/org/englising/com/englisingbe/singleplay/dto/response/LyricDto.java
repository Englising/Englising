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
public class LyricDto {
    private Long lyricId;
    private Boolean isBlank;
    private Float startTime;
    private Float endTime;
    private List<String> lyric;
}
