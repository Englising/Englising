package org.englising.com.englisingbe.auth.dto;

import lombok.Getter;

@Getter
public enum AuthResponseMessage {
    AUTH_GUESTLOGIN_MESSAGE(200, "게스트 로그인이 완료되었습니다"),
    AUTH_USERID_MESSAGE(200, "회원 아이디를 가져왔습니다");

    private final int code;
    private final String message;

    AuthResponseMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
