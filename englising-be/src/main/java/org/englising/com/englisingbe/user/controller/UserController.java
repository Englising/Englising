package org.englising.com.englisingbe.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.global.dto.DefaultResponseDto;
import org.englising.com.englisingbe.user.dto.CustomUserDetails;
import org.englising.com.englisingbe.user.dto.ProfileDto;
import org.englising.com.englisingbe.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @GetMapping("/profile")
    @Operation(
            summary = "회원 프로필 조회",
            description = "회원 프로필을 조회합니다"
    )
    public ResponseEntity<DefaultResponseDto<?>> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {

        ProfileDto profileDto = userService.getProfile(userDetails.getUsername());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DefaultResponseDto<>(HttpStatus.OK.value(), "회원 프로필을 조회합니다.", profileDto));
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
                .body(new DefaultResponseDto<>(HttpStatus.OK.value(), "회원 프로필을 수정합니다", null));
    }




}
