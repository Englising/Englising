package org.englising.com.englisingbe.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GlobalException extends RuntimeException{
    ErrorHttpStatus errorHttpStatus;
}
