package org.englising.com.englisingbe.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.global.dto.DefaultResponseDto;
import org.englising.com.englisingbe.auth.dto.CustomUserDetails;
import org.englising.com.englisingbe.user.dto.NicknameRequestDto;
import org.englising.com.englisingbe.user.dto.NicknameResponseDto;
import org.englising.com.englisingbe.user.dto.ProfileDto;
import org.englising.com.englisingbe.user.dto.UserResponseMessage;
import org.englising.com.englisingbe.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @Operation(
            summary = "회원 프로필 조회",
            description = "회원 프로필을 조회합니다"
    )
    public ResponseEntity<DefaultResponseDto<?>> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        ProfileDto profileDto = userService.getProfile(userDetails.getUsername());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DefaultResponseDto<>(UserResponseMessage.USER_GETPROFILE_MESSAGE.getCode(),
                        UserResponseMessage.USER_GETPROFILE_MESSAGE.getMessage(),
                        profileDto));
    }

    @PutMapping("/profile")
    @Operation(
            summary = "회원 프로필 수정",
            description = "회원 프로필을 수정합니다"
    )
    public ResponseEntity<DefaultResponseDto<?>> updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @RequestBody ProfileDto profileDto) {

        userService.updateProfile(userDetails.getUsername(), profileDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DefaultResponseDto<>(UserResponseMessage.USER_UPDATEPROFILE_MESSAGE.getCode(),
                        UserResponseMessage.USER_UPDATEPROFILE_MESSAGE.getMessage(),
                        null));
    }

    @PostMapping("/nickname")
    @Operation(
            summary = "닉네임 중복 확인",
            description = "닉네임 중복 가능성을 확인합니다"
    )
    public ResponseEntity<DefaultResponseDto<?>> checkNickname(@RequestBody NicknameRequestDto nicknameRequestDto) {
        NicknameResponseDto nicknameResponseDto = userService.checkNickname(nicknameRequestDto.getNickname());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DefaultResponseDto<>(UserResponseMessage.USER_CHECKNICKNAME_MESSAGE.getCode(),
                        UserResponseMessage.USER_CHECKNICKNAME_MESSAGE.getMessage(),
                        nicknameResponseDto));
    }




}
