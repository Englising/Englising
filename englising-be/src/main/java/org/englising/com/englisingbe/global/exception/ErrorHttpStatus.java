package org.englising.com.englisingbe.global.exception;

import lombok.Getter;

@Getter
public enum ErrorHttpStatus {
    UNAUTHORIZED_REFRESH_TOKEN(403,"RefreshToken이 유효하지 않습니다"),
    UNAUTHORIZED_ACCESS_TOKEN(403, "AccessToken이 만료되어 재발급 되었습니다"),
    UNAUTHORIZED_TOKEN(403,"JWT토큰이 유효하지 않습니다"),
    OAUTH2_USER_NOT_FOUND(403, "Oauth2로 인증된 사용자가 없습니다"),
    NO_MATCHING_TRACK(404, "일치하는 노래가 없습니다"),
    NO_MATCHING_HINT(404, "일치하는 힌트가 없습니다"),
    NO_MACHING_SINGLEPLAY(404, "일치하는 싱글플레이 게임이 없습니다"),
    NO_MATCHING_SINGLEPLAYWORD(404, "일치하는 플레이 단어가 없습니다"),
    NO_MATCHING_MULTIPLAYGAME(404, "일치하는 게임이 없습니다"),
    FULL_MULTIPLAY_ROOM(404, "멀티플레이 게임의 정원이 이미 완료되었습니다"),
    USER_ALREADY_EXISTS(404, "이미 참여중인 유저입니다."),
    NO_MATCHING_LYRIC(404, "일치하는 가사가 없습니다"),
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
