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
import org.englising.com.englisingbe.global.dto.DefaultResponseDto;
import org.englising.com.englisingbe.global.util.Genre;
import org.englising.com.englisingbe.global.util.ResponseMessage;
import org.englising.com.englisingbe.multiplay.dto.request.MultiPlayRequestDto;
import org.englising.com.englisingbe.multiplay.dto.response.MultiPlayDetailResponseDto;
import org.englising.com.englisingbe.multiplay.dto.response.MultiPlayListResponseDto;
import org.englising.com.englisingbe.multiplay.entity.MultiPlay;
import org.englising.com.englisingbe.multiplay.service.MultiPlayServiceImpl;
import org.englising.com.englisingbe.multiplay.service.MultiPlaySetterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    //TODO delete---------------------------------------------
    private final MultiPlaySetterService multiPlaySetterService;
    @GetMapping("/test")
    public ResponseEntity testingLyric(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(multiPlaySetterService.getMultiPlaySentenceListFromTrack(158L));
    }//---------------------------------------------------------

    @GetMapping("/rooms")
    @Operation(
        summary = "멀티플레이 대기방 리스트 조회",
        description = "genre 파라미터로 멀티플레이 방 장르를 보내주세요. 페이지네이션은 아직입니다."
    )
    @Parameters({
        @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.COOKIE),
        @Parameter(name = "genre", description = "멀티플레이 방 장르"),
        @Parameter(name = "page", description = "페이지 번호", in = ParameterIn.QUERY),
        @Parameter(name = "size", description = "(선택적) 페이지당 컨텐츠 개수, 기본 10", in = ParameterIn.QUERY)
    })
    @ApiResponse(responseCode = "200", description = "Successful operation",
        content = @Content(
            mediaType = "application/json"
        )
    )
    public ResponseEntity getMultiPlayList(@RequestParam(required = false) Genre genre, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                DefaultResponseDto.<List<MultiPlayListResponseDto>>builder()
                    .status(ResponseMessage.MULTIPLAY_LIST_SUCCESS.getCode())
                    .message(ResponseMessage.MULTIPLAY_LIST_SUCCESS.getMessage())
                    .data(multiPlayService.getMultiPlayWaitingList(genre, page, size))
                    .build()
            );
    }

    @PostMapping
    @Operation(
        summary = "멀티플레이 방 만들기",
        description = "멀티플레이 방 생성 시 필요한 방 이름, 총 인원, 장르를 가져옵니다"
    )
    @Parameters({
        @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.COOKIE),
    })
    @ApiResponse(responseCode = "200", description = "Successful operation",
        content = @Content(
            mediaType = "application/json"
        )
    )
    public ResponseEntity createMultiPlay(@RequestBody MultiPlayRequestDto requestDto) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                DefaultResponseDto.<Long>builder()
                    .status(ResponseMessage.MULTIPLAY_CREATE_SUCCESS.getCode())
                    .message(ResponseMessage.MULTIPLAY_CREATE_SUCCESS.getMessage())
                    .data(multiPlayService.createMultiPlay(requestDto, 1L))
                    .build()
            );
    }

    @GetMapping("/{multiplayId}")
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
    public ResponseEntity getMultiPlayById(@PathVariable Long multiplayId) {
        //TODO 사용자 입장 알림 (websocket)
        messagingTemplate.convertAndSend("/sub/enter/" + multiplayId, "entering");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                DefaultResponseDto.<MultiPlayDetailResponseDto>builder()
                    .status(ResponseMessage.MULTIPLAY_JOIN_SUCCESS.getCode())
                    .message(ResponseMessage.MULTIPLAY_JOIN_SUCCESS.getMessage())
                    .data(multiPlayService.getMultiPlayById(multiplayId, 1L))
                    .build()
            );
    }

    @GetMapping("/{multiplayId}/result")
    @Operation(
        summary = "멀티플레이 종료 및 결과",
        description = "멀티플레이 성공 여부를 반환합니다."
    )
    @Parameters({
        @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.COOKIE),
    })
    public ResponseEntity getMultiPlayResult(@PathVariable Long multiplayId) {
        Boolean result = multiPlayService.getMultiPlayResult(multiplayId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                DefaultResponseDto.<Boolean>builder()
                    .status(ResponseMessage.MULTIPLAY_RESULT_SUCCESS.getCode())
                    .message(ResponseMessage.MULTIPLAY_RESULT_SUCCESS.getMessage())
                    .data(result)
                    .build()
            );
    }

    @GetMapping("/image")
    @Operation(
            summary = "멀티플레이 방 생성 이미지 생성",
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