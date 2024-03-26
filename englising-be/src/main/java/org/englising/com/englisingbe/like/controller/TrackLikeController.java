package org.englising.com.englisingbe.like.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.auth.dto.CustomUserDetails;
import org.englising.com.englisingbe.global.dto.DefaultResponseDto;
import org.englising.com.englisingbe.like.dto.TrackLikeRequestDto;
import org.englising.com.englisingbe.like.dto.TrackLikeResponseDto;
import org.englising.com.englisingbe.like.dto.TrackResponseMessage;
import org.englising.com.englisingbe.like.service.TrackLikeServiceImpl;
import org.englising.com.englisingbe.word.dto.WordLikeRequestDto;
import org.englising.com.englisingbe.word.dto.WordLikeResponseDto;
import org.englising.com.englisingbe.word.dto.WordResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TrackLikeController {

    private final TrackLikeServiceImpl trackLikeService;

    @PostMapping("/track/like")
    @Operation(
            summary = "노래 즐겨찾기",
            description = "노래 즐겨찾기를 등록하고 즐겨찾기 상태를 반환합니다"
    )
    @Parameters({
            @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.COOKIE)
    })
    public ResponseEntity<DefaultResponseDto<?>> likeTrack(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                          @RequestBody TrackLikeRequestDto trackLikeRequestDto) {

//        Long userId = Long.parseLong(userDetails.getUsername());
//        TrackLikeResponseDto trackLikeResponseDto = trackLikeService.likeTrack(trackLikeRequestDto.getTrackId(), userId);

        TrackLikeResponseDto trackLikeResponseDto = trackLikeService.likeTrack(trackLikeRequestDto.getTrackId(), 1L);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DefaultResponseDto<>(TrackResponseMessage.TRACK_LIKE_MESSAGE.getCode(),
                        TrackResponseMessage.TRACK_LIKE_MESSAGE.getMessage(),
                        trackLikeResponseDto));
    }
}
