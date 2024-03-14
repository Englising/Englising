package org.englising.com.englisingbe.user.controller;

import lombok.RequiredArgsConstructor;
import org.apache.juli.logging.Log;
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

    // todo. ResponseEntity로 추후 수정

    // 게스트 로그인
//    @PostMapping("/guest")
//    public ResponseEntity<JwtResponseDto> guestLogin(@RequestBody UserSignUpDto userSignUpDto) throws Exception {
//        userService.signUp(userSignUpDto);
//
//
//        return ResponseEntity<JwtResponseDto>;
//        //todo. api에 맞춰서 수정. 지금은 test만
//    }

    @PostMapping("/guest")
    public ResponseEntity<JwtResponseDto> guestLogin() throws Exception {
        userService.signUp();

        //이메일로 회원찾기 해서 회원 아이디 가져옴
        String userId = "12"; //임시로
        String accessToken = jwtTokenProvider.createAccessToken(userId);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new JwtResponseDto(accessToken, refreshToken));
        //todo. api에 맞춰서 수정. DefaultResponse로 수정
    }

}
