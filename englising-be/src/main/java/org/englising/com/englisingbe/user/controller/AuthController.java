package org.englising.com.englisingbe.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.global.dto.DefaultResponseDto;
import org.englising.com.englisingbe.jwt.CookieUtil;
import org.englising.com.englisingbe.jwt.JwtResponseDto;
import org.englising.com.englisingbe.user.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final CookieUtil cookieUtil;

    @PostMapping("/guest")
    public ResponseEntity<DefaultResponseDto<?>> guest(HttpServletResponse response) throws Exception {

        JwtResponseDto jwtResponseDto = userService.guest();

        Cookie accessCookie = cookieUtil.createAccessCookie("Authorization", jwtResponseDto.getAccessToken());
        Cookie refreshCookie = cookieUtil.createRefreshCookie("Authorization-refresh", jwtResponseDto.getRefreshToken());
        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
        // todo. 에러 처리 추가

        // ResponseEntity에 헤더와 본문 설정
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new DefaultResponseDto<>(200, "토큰 발급이 완료되었습니다.", jwtResponseDto));
    }

    @GetMapping("/login")
    public ResponseEntity<DefaultResponseDto<?>> getUserId(HttpServletRequest request) {

        Long userId = userService.getUserID(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DefaultResponseDto<>(200, "회원 아이디를 가져왔습니다", userId));
    }
}
