package org.englising.com.englisingbe.singleplay.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RightWordCntDto {
    private int totalWordCnt;
    private int rightWordCnt;
}
