package org.englising.com.englisingbe.global.exception;

import lombok.Getter;

@Getter
public enum ErrorHttpStatus {
    // 인증, 인가 관련 에러 코드
    UNAUTHORIZED_TOKEN(403,"JWT토큰이 유효하지 않습니다"),
    UNAUTHORIZED_ACCESS_TOKEN(403, "AccessToken이 만료되어 재발급 되었습니다"),
    UNAUTHORIZED_REFRESH_TOKEN(403,"RefreshToken이 유효하지 않습니다"),
    OAUTH2_USER_NOT_FOUND(403, "Oauth2로 인증된 사용자가 없습니다"),
    COOKIE_NOT_FOUND(403, "Cookie가 존재하지 않습니다"),
    // User 관련 에러 코드
    USER_NOT_FOUND(410, "회원을 찾을 수 없습니다"),
    USER_NICKNAME_DUPLICATED(411, "중복된 닉네임이 존재합니다"),
    // 자원 조회 관련 에러 코드
    NO_MATCHING_TRACK(420, "일치하는 노래가 없습니다"),
    NO_MATCHING_LYRIC(421, "일치하는 가사가 없습니다"),
    NO_MATCHING_HINT(422, "일치하는 힌트가 없습니다"),
    NO_MATCHING_WORD(423, "해당 단어가 존재하지 않습니다"),
    // SinglePlayGame 관련 에러 코드
    NO_MACHING_SINGLEPLAY(430, "일치하는 싱글플레이 게임이 없습니다"),
    NO_MATCHING_SINGLEPLAYWORD(431, "일치하는 플레이 단어가 없습니다"),
    // MultiPlayGame 관련 에러 코드
    NO_MATCHING_MULTIPLAYGAME(440, "일치하는 게임이 없습니다"),
    FULL_MULTIPLAY_ROOM(441, "멀티플레이 게임의 정원이 이미 완료되었습니다"),
    USER_ALREADY_EXISTS(442, "이미 참여중인 유저입니다"),
    NOT_PARTICIPATING_USER(443, "멀티플레이에 참여중인 유저가 아닙니다"),

    END_OR_LINE(900, "세미콜론 귀찮아서 만듦");

    private final int code;
    private final String message;

    ErrorHttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
