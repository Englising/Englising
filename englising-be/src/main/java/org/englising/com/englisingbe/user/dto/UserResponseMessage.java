package org.englising.com.englisingbe.user.dto;

import lombok.Getter;

@Getter
public enum UserResponseMessage {
    USER_GETPROFILE_MESSAGE(200, "회원 프로필을 가져왔습니다"),
    USER_UPDATEPROFILE_MESSAGE(200, "회원 프로필을 수정하였습니다"),
    USER_CHECKNICKNAME_MESSAGE(200, "닉네임 중복확인이 완료되었습니다"),
    USER_GETRANDOM_MESSAGE(200, "랜덤 이미지를 가져왔습니다");

    private final int code;
    private final String message;

    UserResponseMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }
}