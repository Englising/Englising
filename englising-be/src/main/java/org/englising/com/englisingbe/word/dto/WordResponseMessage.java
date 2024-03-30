package org.englising.com.englisingbe.word.dto;

import lombok.Getter;

@Getter
public enum WordResponseMessage {
    WORD_GETlIST_MESSAGE(200, "단어장 리스트를 가져왔습니다"),
    WORD_LIKE_MESSAGE(200, "단어장 즐겨찾기를 등록하였습니다");

    private final int code;
    private final String message;

    WordResponseMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }
}