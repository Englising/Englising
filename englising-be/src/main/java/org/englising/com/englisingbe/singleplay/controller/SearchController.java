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
import org.englising.com.englisingbe.auth.dto.CustomUserDetails;
import org.englising.com.englisingbe.global.dto.DefaultResponseDto;
import org.englising.com.englisingbe.global.util.PlayListType;
import org.englising.com.englisingbe.global.util.ResponseMessage;
import org.englising.com.englisingbe.singleplay.dto.response.PlayListDto;
import org.englising.com.englisingbe.singleplay.dto.response.TrackResponseDto;
import org.englising.com.englisingbe.singleplay.service.SinglePlayServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Search Controller", description = "싱글플레이 노래 검색 컨트롤러")
@RestController
@RequiredArgsConstructor
public class SearchController {
    private final SinglePlayServiceImpl singlePlayService;

    @GetMapping("/search")
    @Operation(
            summary = "싱글플레이가 가능한 노래의 플레이리스트 조회",
            description = "type 파라미터로 플레이리스트의 종류를 보내주세요. 페이지네이션이 적용되어 있습니다"
    )
    @Parameters({
            @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.COOKIE),
            @Parameter(name = "keyword", description = "검색어", in = ParameterIn.QUERY),
            @Parameter(name = "page", description = "페이지 번호", in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "(선택적) 페이지당 컨텐츠 개수, 기본 10", in = ParameterIn.QUERY)
    })
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TrackResponseDto.class)
            )
    )
    public ResponseEntity getPlaylists(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam String keyword, @RequestParam Integer page, @RequestParam Integer size){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        DefaultResponseDto.<PlayListDto>builder()
                                .status(ResponseMessage.SINGLEPLAY_PLAYLIST_SUCCESS.getCode())
                                .message(ResponseMessage.SINGLEPLAY_PLAYLIST_SUCCESS.getMessage())
                                .data(singlePlayService.getSearchTracks(keyword, page, size, 1L))
                                .build()
                );
    }
}
