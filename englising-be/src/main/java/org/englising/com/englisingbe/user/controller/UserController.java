package org.englising.com.englisingbe.user.controller;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.global.dto.DefaultResponseDto;
import org.englising.com.englisingbe.jwt.JwtResponseDto;
import org.englising.com.englisingbe.user.dto.KakaoRequestDto;
import org.englising.com.englisingbe.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/guest")
    public ResponseEntity<DefaultResponseDto<?>> guest() throws Exception {

        JwtResponseDto jwtResponseDto = userService.guest();
        // todo. 에러 처리 추가

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new DefaultResponseDto<>(200, "토큰 발급이 완료되었습니다.", jwtResponseDto));
    }

    // todo. 카카오 로그인 post 요청
    @PostMapping("/kakao")
    public ResponseEntity<DefaultResponseDto<JwtResponseDto>> kakaoLogin(@RequestBody KakaoRequestDto kakaoRequestDto) {
        JwtResponseDto jwtResponseDto = userService.login(kakaoRequestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DefaultResponseDto<>(200, "토큰 발급이 완료되었습니다.", jwtResponseDto));

    }

    // todo. RefreshToken으로 AccessToken 재발급 Get 요청
    @GetMapping("/refresh")
    public ResponseEntity<?> getAccessTokenByRefreshToken() {
        JwtResponseDto jwtResponseDto = null; // todo.수정

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DefaultResponseDto<>(200, "토큰 발급이 완료되었습니다.", jwtResponseDto));
    }
    
    //todo. 로그아웃

}
