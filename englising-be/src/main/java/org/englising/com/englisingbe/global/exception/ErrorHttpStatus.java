package org.englising.com.englisingbe.global.exception;

import lombok.Getter;

@Getter
public enum ErrorHttpStatus {
    UNAUTHORIZED_REFRESH_TOKEN(403,"RefreshToken이 유효하지 않습니다"),
    UNAUTHORIZED_TOKEN(403,"JWT토큰이 유효하지 않습니다"),

    USER_NOT_FOUND(403, "회원을 찾을 수 없습니다.");





    private final int code;
    private final String message;

    ErrorHttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
