package org.englising.com.englisingbe.multiplay.dto.socket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AnswerDto {
    private int alphabetIndex;
    private String alphabet;
}
