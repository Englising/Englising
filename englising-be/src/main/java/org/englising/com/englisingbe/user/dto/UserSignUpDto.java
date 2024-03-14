package org.englising.com.englisingbe.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.englising.com.englisingbe.user.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

// 게스트 로그인 Api에 RequestBody로 사용할 UserSignUpDto
@NoArgsConstructor
@Getter
public class UserSignUpDto {

    private String email;
    private String nickname;
    private String profileImg;
    private String type;

}

