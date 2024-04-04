package org.englising.com.englisingbe.multiplay.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.auth.dto.CustomUserDetails;
import org.englising.com.englisingbe.global.dto.DefaultResponseDto;
import org.englising.com.englisingbe.global.util.Genre;
import org.englising.com.englisingbe.global.util.ResponseMessage;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlayUser;
import org.englising.com.englisingbe.multiplay.dto.request.MultiPlayRequestDto;
import org.englising.com.englisingbe.multiplay.dto.response.MultiPlayDetailResponseDto;
import org.englising.com.englisingbe.multiplay.dto.response.MultiPlayListResponseDto;
import org.englising.com.englisingbe.multiplay.entity.MultiPlay;
import org.englising.com.englisingbe.multiplay.service.MultiPlayServiceImpl;
import org.englising.com.englisingbe.multiplay.service.MultiPlaySetterService;
import org.englising.com.englisingbe.user.entity.User;
import org.englising.com.englisingbe.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/multiplay")
public class MultiPlayController {
    private final MultiPlayServiceImpl multiPlayService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/rooms")
    @Operation(
        summary = "멀티플레이 대기방 리스트 조회",
        description = "genre 파라미터로 멀티플레이 방 장르를 보내주세요."
    )
    @Parameters({
        @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.COOKIE),
        @Parameter(name = "genre", description = "멀티플레이 방 장르(all, dance, rnb, rock, pop)")
    })
    @ApiResponse(responseCode = "200", description = "Successful operation",
        content = @Content(
            mediaType = "application/json"
        )
    )
    public ResponseEntity getMultiPlayList(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(required = false) Genre genre){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                DefaultResponseDto.<List<MultiPlayListResponseDto>>builder()
                    .status(ResponseMessage.MULTIPLAY_LIST_SUCCESS.getCode())
                    .message(ResponseMessage.MULTIPLAY_LIST_SUCCESS.getMessage())
                    .data(multiPlayService.getMultiPlayWaitingList(genre))
                    .build()
            );
    }

    @PostMapping
    @Operation(
        summary = "멀티플레이 방 만들기",
        description = "멀티플레이 방 생성 요청을 합니다."
    )
    @Parameters({
        @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.COOKIE),
    })
    @ApiResponse(responseCode = "200", description = "Successful operation",
        content = @Content(
            mediaType = "application/json"
        )
    )
    public ResponseEntity createMultiPlay(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody MultiPlayRequestDto requestDto) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                DefaultResponseDto.<Long>builder()
                    .status(ResponseMessage.MULTIPLAY_CREATE_SUCCESS.getCode())
                    .message(ResponseMessage.MULTIPLAY_CREATE_SUCCESS.getMessage())
                    .data(multiPlayService.createMultiPlay(requestDto, Long.parseLong(userDetails.getUsername())))
                    .build()
            );
    }

    @GetMapping("/{multiPlayId}")
    @Operation(
            summary = "멀티플레이 방 정보 조회",
            description = "현재 진행 중인 멀티플레이 방의 정보를 조회합니다."
    )
    @Parameters({
            @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.COOKIE),
    })
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(
                    mediaType = "application/json"
            )
    )
    public ResponseEntity getMultiPlayById(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long multiPlayId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        DefaultResponseDto.<MultiPlayDetailResponseDto>builder()
                                .status(ResponseMessage.MULTIPLAY_JOIN_SUCCESS.getCode())
                                .message(ResponseMessage.MULTIPLAY_JOIN_SUCCESS.getMessage())
                                .data(multiPlayService.getMultiPlayById(multiPlayId, Long.parseLong(userDetails.getUsername())))
                                .build()
                );
    }

    @PostMapping("/{multiplayId}")
    @Operation(
        summary = "멀티플레이 방 참여",
        description = "멀티플레이 방 아이디로 해당 방을 조회 후 참여합니다."
    )
    @Parameters({
        @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.COOKIE),
    })
    @ApiResponse(responseCode = "200", description = "Successful operation",
        content = @Content(
            mediaType = "application/json"
        )
    )
    public ResponseEntity enterMultiPlayById(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long multiplayId) {
        multiPlayService.enterMultiPlay(multiplayId, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                DefaultResponseDto.<String>builder()
                    .status(ResponseMessage.MULTIPLAY_JOIN_SUCCESS.getCode())
                    .message(ResponseMessage.MULTIPLAY_JOIN_SUCCESS.getMessage())
                    .data("멀티플레이에 성공적으로 참여했습니다.")
                    .build()
            );
    }

    @DeleteMapping("/{multiplayId}")
    @Operation(
            summary = "멀티플레이 대기방에서 이용자가 떠남 요청",
            description = "멀티플레이 대기방에 참여중인 이용자가 대기방을 나갈 때 요청합니다."
    )
    @Parameters({
            @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.COOKIE),
    })
    public ResponseEntity leaveMultiPlayGame(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long multiplayId) {
        multiPlayService.leaveGame(multiplayId, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(DefaultResponseDto.<String>builder()
                        .status(HttpStatus.OK.value())
                        .message("게임을 떠났습니다.")
                        .build());
    }

    @GetMapping("/{multiplayId}/game")
    @Operation(
            summary = "멀티플레이 시작 요청",
            description = "멀티플레이 게임 진행 시작을 요청합니다. 방장만 가능합니다."
    )
    @Parameters({
            @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.COOKIE),
    })
    public ResponseEntity startMultiPlayGame(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long multiplayId) {
        multiPlayService.startGame(multiplayId, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(DefaultResponseDto.<String>builder()
                        .status(HttpStatus.OK.value())
                        .message("게임을 성공적으로 시작했습니다.")
                        .build());
    }

    @GetMapping("/image")
    @Operation(
            summary = "멀티플레이 방 이미지 생성",
            description = "랜덤한 멀티플레이 방 이미지를 반환합니다."
    )
    @Parameters({
            @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.COOKIE),
    })
    public ResponseEntity getRandomImage(){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                    DefaultResponseDto.<String>builder()
                            .status(HttpStatus.OK.value())
                            .message("")
                            .data(multiPlayService.createRandomImg())
                            .build()
            );
    }
}