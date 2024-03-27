package org.englising.com.englisingbe.multiplay.dto.socket;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoundResultDto {
    private int round;
    private boolean isCorrect;
}
