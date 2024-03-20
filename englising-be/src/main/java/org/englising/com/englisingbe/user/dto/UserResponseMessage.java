package org.englising.com.englisingbe.user.dto;

import lombok.Getter;

@Getter
public enum UserResponseMessage {
    USER_GETPROFILE_MESSAGE(200, "플레이리스트를 성공적으로 가져왔습니다.");
//    USER_UPDATEPROFILE_MESSAGE(200, "회원 프로필을 수정합니다")


    private final int code;
    private final String message;

    UserResponseMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }
}