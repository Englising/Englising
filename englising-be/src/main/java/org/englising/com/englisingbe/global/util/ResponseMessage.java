package org.englising.com.englisingbe.global.util;

import lombok.Getter;

@Getter
public enum ResponseMessage {
    SINGLEPLAY_PLAYLIST_SUCCESS(200, "플레이리스트를 성공적으로 가져왔습니다.");

    private final int code;
    private final String message;
    ResponseMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
