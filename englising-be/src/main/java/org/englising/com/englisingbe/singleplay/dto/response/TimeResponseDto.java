package org.englising.com.englisingbe.singleplay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeResponseDto {
    public Long trackId;
    public List<Float> startTime;
}
