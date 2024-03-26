package org.englising.com.englisingbe.global.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultResponseDto<T> {
    private Integer status;
    private String message;
    private T data;
}

// data의 경우 매번 변경되기 때문에 generic 사용
