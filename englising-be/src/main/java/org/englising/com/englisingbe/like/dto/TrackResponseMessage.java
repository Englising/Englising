package org.englising.com.englisingbe.like.dto;

import lombok.Getter;

@Getter
public enum TrackResponseMessage {
    TRACK_LIKE_MESSAGE(200, "노래 즐겨찾기를 등록하였습니다");

    private final int code;
    private final String message;

    TrackResponseMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
