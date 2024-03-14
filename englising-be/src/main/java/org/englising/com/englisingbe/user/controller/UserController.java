package org.englising.com.englisingbe.user.controller;

import lombok.RequiredArgsConstructor;
import org.apache.juli.logging.Log;
import org.englising.com.englisingbe.global.dto.DefaultResponseDto;
import org.englising.com.englisingbe.jwt.JwtResponseDto;
import org.englising.com.englisingbe.jwt.JwtTokenProvider;
import org.englising.com.englisingbe.user.dto.UserSignUpDto;
import org.englising.com.englisingbe.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/guest")
    public ResponseEntity<DefaultResponseDto<JwtResponseDto>> guestLogin() throws Exception {
        String userId = userService.signUp();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new DefaultResponseDto(200, "토큰 발급이 완료되었습니다.",
                        jwtTokenProvider.getTokens(userId)));
    }

    // todo. 카카오 로그인 post 요청

    // todo. RefreshToken으로 AccessToken 재발급 Get 요청

}
