package org.englising.com.englisingbe.global.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultResponseDto<T> {
    private Integer status;
    private String message;
    private T data;
}
