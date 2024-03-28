package org.englising.com.englisingbe.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.global.dto.DefaultResponseDto;
import org.englising.com.englisingbe.auth.dto.CustomUserDetails;
import org.englising.com.englisingbe.user.dto.*;
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
        Long userId = Long.parseLong(userDetails.getUsername());
        ProfileDto profileDto = userService.getProfile(userId);
//        ProfileDto profileDto = userService.getProfile(419L); //todo. 추후 위로 수정

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
        Long userId = Long.parseLong(userDetails.getUsername());
        userService.updateProfile(userId, profileDto);
//        userService.updateProfile(419L, profileDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DefaultResponseDto<>(UserResponseMessage.USER_UPDATEPROFILE_MESSAGE.getCode(),
                        UserResponseMessage.USER_UPDATEPROFILE_MESSAGE.getMessage(),
                        null));
    }

    @GetMapping("/profile/random")
    @Operation(
            summary = "랜덤 이미지 반환",
            description = "랜덤 이미지와 배경색을 반환합니다"
    )
    public ResponseEntity<DefaultResponseDto<?>> getRandomImg() {

        RandomImgDto randomImgDto = userService.getRandomImg();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DefaultResponseDto<>(UserResponseMessage.USER_GETRANDOM_MESSAGE.getCode(),
                        UserResponseMessage.USER_GETRANDOM_MESSAGE.getMessage(),
                        randomImgDto));
    }


    @PostMapping("/nickname")
    @Operation(
            summary = "닉네임 중복 확인",
            description = "닉네임 중복 가능성을 확인합니다"
    )
    public ResponseEntity<DefaultResponseDto<?>> checkNickname(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               @RequestBody NicknameRequestDto nicknameRequestDto) {
        Long userId = Long.parseLong(userDetails.getUsername());
        NicknameResponseDto nicknameResponseDto = userService.checkNickname(nicknameRequestDto.getNickname(), userId);
//        NicknameResponseDto nicknameResponseDto = userService.checkNickname(nicknameRequestDto.getNickname(), 419L);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DefaultResponseDto<>(UserResponseMessage.USER_CHECKNICKNAME_MESSAGE.getCode(),
                        UserResponseMessage.USER_CHECKNICKNAME_MESSAGE.getMessage(),
                        nicknameResponseDto));
    }
}
