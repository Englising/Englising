package org.englising.com.englisingbe.global.util;

import lombok.Getter;

@Getter
public enum ResponseMessage {
    SINGLEPLAY_PLAYLIST_SUCCESS(200, "플레이리스트를 성공적으로 가져왔습니다."),
    MULTIPLAY_LIST_SUCCESS(200, "멀티플레이 리스트를 성공적으로 가져왔습니다."),
    MULTIPLAY_START_SUCCESS(200, "멀티플레이를 성공적으로 시작했습니다."),
    MULTIPLAY_CREATE_SUCCESS(200, "멀티플레이를 성공적으로 생성했습니다."),
    MULTIPLAY_JOIN_SUCCESS(200, "멀티플레이에 성공적으로 참가했습니다."),
    MULTIPLAY_RESULT_SUCCESS(200, "멀티플레이 결과를 성공적으로 가져왔습니다.");

    private final int code;
    private final String message;
    ResponseMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
