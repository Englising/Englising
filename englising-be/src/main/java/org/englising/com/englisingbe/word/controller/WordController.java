package org.englising.com.englisingbe.word.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.auth.dto.CustomUserDetails;
import org.englising.com.englisingbe.global.dto.DefaultResponseDto;
import org.englising.com.englisingbe.user.dto.ProfileDto;
import org.englising.com.englisingbe.user.dto.UserResponseMessage;
import org.englising.com.englisingbe.word.dto.WordLikeResponseDto;
import org.englising.com.englisingbe.word.dto.WordListResponseDto;
import org.englising.com.englisingbe.word.dto.WordListType;
import org.englising.com.englisingbe.word.dto.WordResponseMessage;
import org.englising.com.englisingbe.word.service.WordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/word")
public class WordController {

    private final WordService wordService;
//
    @GetMapping("/list")
    @Operation(
            summary = "단어장 리스트 조회",
            description = "단어장 목록을 타입별로 (좋아요한 단어, 최근 플레이한 단어, 검색한 단어) 조회합니다"
    )
    public ResponseEntity<DefaultResponseDto<WordListResponseDto>> getWordList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam WordListType type, @RequestParam Integer page, @RequestParam Integer size) {

//        WordListResponseDto wordListResponseDto = wordService.getWordList(type, page, size, userDetails.getUsername());
        WordListResponseDto wordListResponseDto = wordService.getWordList(type, page, size, "1");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DefaultResponseDto<>(WordResponseMessage.WORD_GETlIST_MESSAGE.getCode(),
                        WordResponseMessage.WORD_GETlIST_MESSAGE.getMessage(),
                        wordListResponseDto));
    }

    @PostMapping("/like")
    @Operation(
            summary = "단어 즐겨찾기",
            description = "단어 즐겨찾기를 등록하고 즐겨찾기 상태를 반환합니다"
    )
    public ResponseEntity<DefaultResponseDto<?>> likeWord(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                          @RequestParam Long wordId) {

//        WordLikeResponseDto wordLikeResponseDto = wordService.likeWord(wordId, userDetails.getUsername());
        WordLikeResponseDto wordLikeResponseDto = wordService.likeWord(1L, "1");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DefaultResponseDto<>(WordResponseMessage.WORD_LIKE_MESSAGE.getCode(),
                        WordResponseMessage.WORD_LIKE_MESSAGE.getMessage(),
                        wordLikeResponseDto));
    }

}
