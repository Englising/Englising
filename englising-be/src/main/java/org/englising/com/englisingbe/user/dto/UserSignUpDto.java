package org.englising.com.englisingbe.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 게스트 로그인 Api에 RequestBody로 사용할 UserSignUpDto
@Getter
@Builder
@AllArgsConstructor
public class UserSignUpDto {

    @NotBlank
    private String email;
    @NotBlank
    private String nickname;
    @NotBlank
    private String profileImg;
    private String type;

    public User toEntity() {
        return User.builder()
                .email("random@email.com") // todo. uuid 사용 ex)오늘 날짜 + uuid + @email.com
                .nickname("tempNickname")
                .profileImg("tempProfileImgUrl")
                .type("GUEST") // default로 GUEST로 되어있으면 빼기
                .build();
    }
}
