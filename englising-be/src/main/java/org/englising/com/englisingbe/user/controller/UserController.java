package org.englising.com.englisingbe.user.controller;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.user.dto.UserSignUpDto;
import org.englising.com.englisingbe.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    // todo. ResponseEntity로 추후 수정

    // 비회원 로그인
    @PostMapping("/guest")
    public String guestLogin(@RequestBody UserSignUpDto userSignUpDto) {
        userService.signUp(userSignUpDto);
        return "게스트 로그인 성공";
        //todo. api에 맞춰서 수정. 지금은 test만
    }


}
