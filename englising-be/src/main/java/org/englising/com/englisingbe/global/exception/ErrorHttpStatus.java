package org.englising.com.englisingbe.global.exception;

import lombok.Getter;

@Getter
public enum ErrorHttpStatus {
    UNAUTHORIZED_REFRESH_TOKEN(403,"RefreshToken이 유효하지 않습니다"),
    UNAUTHORIZED_TOKEN(403,"JWT토큰이 유효하지 않습니다"),
    NO_MATCHING_TRACK(404, "일치하는 노래가 없습니다"),
    NO_MATCHING_MULTIPLAYGAME(404, "일치하는 게임이 없습니다"),
    USER_ALREADY_EXISTS(404, "이미 참여중인 유저입니다."),
    USER_NOT_FOUND(403, "회원을 찾을 수 없습니다."),
    USER_NICKNAME_DUPLICATED(403, "중복된 닉네임이 존재합니다."),
    COOKIE_NOT_FOUND(403, "Cookie가 존재하지 않습니다."),
    WORD_NOT_FOUND(403, "해당 단어가 존재하지 않습니다.");

    private final int code;
    private final String message;

    ErrorHttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
