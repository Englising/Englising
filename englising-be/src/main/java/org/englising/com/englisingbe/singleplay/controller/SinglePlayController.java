package org.englising.com.englisingbe.singleplay.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.global.dto.DefaultResponseDto;
import org.englising.com.englisingbe.global.util.PlayListType;
import org.englising.com.englisingbe.singleplay.dto.request.SinglePlayRequestDto;
import org.englising.com.englisingbe.singleplay.dto.request.WordCheckRequestDto;
import org.englising.com.englisingbe.singleplay.dto.response.*;
import org.englising.com.englisingbe.singleplay.service.SinglePlayServiceImpl;
import org.englising.com.englisingbe.user.dto.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "SinglePlay Controller", description = "싱글플레이 게임 관련 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/singleplay")
public class SinglePlayController {
    private final SinglePlayServiceImpl singlePlayService;

    @GetMapping("/playlist")
    // API 상세 정보 기술
    @Operation(
            summary = "싱글플레이가 가능한 노래의 플레이리스트 조회",
            description = "type 파라미터로 플레이리스트의 종류를 보내주세요. 페이지네이션이 적용되어 있습니다"
    )
    // API Parameter 정보 작성
    @Parameters({
            @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.COOKIE),
            @Parameter(name = "type", description = "플레이리스트 종류 : RECOMMEND(추천된), LIKE(좋아요 한), RECENT(최근 플레이 한)", in = ParameterIn.QUERY),
            @Parameter(name = "page", description = "페이지 번호", in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "(선택적) 페이지당 컨텐츠 개수, 기본 10", in = ParameterIn.QUERY)
    })
    // API Response 정보 기술
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PlayListResponseDto.class)
            )
    )
    public ResponseEntity getPlaylists(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam PlayListType type, @RequestParam Integer page, @RequestParam Integer size){
        DefaultResponseDto<PlayListResponseDto> response = new DefaultResponseDto<>();
        return new ResponseEntity<>(HttpStatus.OK);
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
                    schema = @Schema(implementation = StartResponseDto.class)
            )
    )
    public ResponseEntity startSingleplay(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody SinglePlayRequestDto startDto){
        return new ResponseEntity(HttpStatus.OK);
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
    public ResponseEntity checkSingleplayWord(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody WordCheckRequestDto wordCheckRequestDto){
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/result")
    @Operation(
            summary = "싱글플레이 종료",
            description = "싱글플레이를 하기 위해 필요한 노래 정보, 단어 출제 내용, 가사를 가져옵니다"
    )
    @Parameters({
            @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.COOKIE),
    })
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResultResponseDto.class)
            )
    )
    public ResponseEntity getSingleplayResult(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody Long singleplayId){
        return new ResponseEntity(HttpStatus.OK);
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
    public ResponseEntity getLyricStartTimes(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long trackId){
        return new ResponseEntity(HttpStatus.OK);
    }
}

