package org.englising.com.englisingbe.multiplay.dto.gameresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MultiPlayGameDefaultResponse<T> {
    private Integer status;
    private Integer round;
    private String type;
    private T content;
}
