package org.englising.com.englisingbe.singleplay.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.englising.com.englisingbe.global.dto.DefaultResponseDto;
import org.englising.com.englisingbe.global.util.PlayListType;
import org.englising.com.englisingbe.global.util.ResponseMessage;
import org.englising.com.englisingbe.singleplay.dto.request.SinglePlayRequestDto;
import org.englising.com.englisingbe.singleplay.dto.request.SinglePlayResultRequestDto;
import org.englising.com.englisingbe.singleplay.dto.request.WordCheckRequestDto;
import org.englising.com.englisingbe.singleplay.dto.response.*;
import org.englising.com.englisingbe.singleplay.service.SinglePlayServiceImpl;
import org.englising.com.englisingbe.auth.dto.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "SinglePlay Controller", description = "싱글플레이 게임 관련 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/singleplay")
@Slf4j
public class SinglePlayController {
    private final SinglePlayServiceImpl singlePlayService;

    @GetMapping("/playlist")
    @Operation(
            summary = "사용자가 좋아요 했거나 싱글플레이 진행 전적이 있는 노래의 리스트 조회",
            description = "type 파라미터로 플레이리스트의 종류를 보내주세요. 페이지네이션이 적용되어 있습니다"
    )
    @Parameters({
            @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.COOKIE),
            @Parameter(name = "type", description = "플레이리스트 종류 : LIKE(좋아요 한), RECENT(최근 플레이 한)", in = ParameterIn.QUERY),
            @Parameter(name = "page", description = "페이지 번호", in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "(선택적) 페이지당 컨텐츠 개수, 기본 10", in = ParameterIn.QUERY)
    })
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PlayListDto.class)
            )
    )
    public ResponseEntity getPlaylists(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(defaultValue = "like") PlayListType type, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        DefaultResponseDto.<PlayListDto>builder()
                                .status(ResponseMessage.SINGLEPLAY_PLAYLIST_SUCCESS.getCode())
                                .message(ResponseMessage.SINGLEPLAY_PLAYLIST_SUCCESS.getMessage())
                                .data(singlePlayService.getPlayList(type, page, size, Long.parseLong(userDetails.getUsername())))
                                .build()
                );
    }

    @GetMapping("/playlist/recommend")
    @Operation(
        summary = "사용자의 추천 플레이리스트 조회",
        description = "사용자에 따른 18개의 추천 플레이리스트를 반환합니다."
    )
    @Parameters({
        @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.COOKIE)
    })
    @ApiResponse(responseCode = "200", description = "Successful operation",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = TrackResponseDto.class)
        )
    )
    public ResponseEntity getRecommendedPlaylist(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                DefaultResponseDto.<List<TrackResponseDto>>builder()
                    .status(ResponseMessage.SINGLEPLAY_PLAYLIST_SUCCESS.getCode())
                    .message(ResponseMessage.SINGLEPLAY_PLAYLIST_SUCCESS.getMessage())
                    .data(singlePlayService.getRecommendedTracks(Long.parseLong(userDetails.getUsername())))
                    .build()
            );
    }

    @PostMapping()
    @Operation(
            summary = "싱글플레이 시작",
            description = "싱글플레이를 하기 위해 필요한 노래 정보, 단어 출제 내용, 가사를 가져옵니다"
    )
    @Parameters({
            @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.COOKIE),
    })
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SinglePlayResponseDto.class)
            )
    )
    public ResponseEntity startSinglePlay(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody SinglePlayRequestDto startDto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        DefaultResponseDto.<SinglePlayResponseDto>builder()
                                .status(HttpStatus.OK.value())
                                .message("싱글플레이 게임 시작을 위한 정보를 가져왔습니다.")
                                .data(singlePlayService.createSinglePlay(Long.parseLong(userDetails.getUsername()), startDto.getTrackId(), startDto.getLevel()))
                                .build()
                );
    }

    @PostMapping("/word-check")
    @Operation(
            summary = "싱글플레이 단어 답안 확인",
            description = "싱글플레이 플레이 중 사용자가 입력한 단어 답안의 정답 유무를 확인합니다."
    )
    @Parameters({
            @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.COOKIE),
    })
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = WordCheckResponseDto.class)
            )
    )
    public ResponseEntity checkSinglePlayWord(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody WordCheckRequestDto wordCheckRequestDto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        DefaultResponseDto.<WordCheckResponseDto>builder()
                                .status(HttpStatus.OK.value())
                                .message("단어 정답을 확인 했습니다.")
                                .data(singlePlayService.checkWord(wordCheckRequestDto, Long.parseLong(userDetails.getUsername())))
                                .build()
                );
    }

    @PostMapping("/result")
    @Operation(
            summary = "싱글플레이 종료",
            description = "싱글플레이를 종료하고 현재까지 맞춘 단어와 개수, 노래 정보를 가져옵니다"
    )
    @Parameters({
            @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.COOKIE),
    })
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SinglePlayResponseDto.class)
            )
    )
    public ResponseEntity getSingleplayResult(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody SinglePlayResultRequestDto singlePlay){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        DefaultResponseDto.<SinglePlayResultResponseDto>builder()
                                .status(HttpStatus.OK.value())
                                .message("싱글플레이 결과를 가져왔습니다.")
                                .data(singlePlayService.getSinglePlayResult(singlePlay.getSinglePlayId(), Long.parseLong(userDetails.getUsername())))
                                .build()
                );
    }

    @GetMapping("/track/{trackId}")
    @Operation(
            summary = "싱글플레이에 선택된 곡 가사의 시작 시간 조회",
            description = "해당하는 싱글플레이 trackId를 보내면 해당 노래 가사의 문장 단위로 시작 시간 리스트를 반환합니다."
    )
    @Parameters({
            @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.HEADER),
    })
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TimeResponseDto.class)
            )
    )
    public ResponseEntity getLyricStartTimes(@PathVariable Long trackId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        DefaultResponseDto.<TimeResponseDto>builder()
                                .status(HttpStatus.OK.value())
                                .message("노래 가사 시작 시간을 가져왔습니다.")
                                .data(singlePlayService.getLyricStartTimes(trackId))
                                .build()
                );
    }
}

