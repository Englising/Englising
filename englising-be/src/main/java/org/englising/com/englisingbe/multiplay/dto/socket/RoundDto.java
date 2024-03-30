package org.englising.com.englisingbe.multiplay.dto.socket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.englising.com.englisingbe.global.util.MultiPlayStatus;

@Data
@Builder
@AllArgsConstructor
public class RoundDto<T> {
    private int round;
    private MultiPlayStatus status;
    private T data;
}
