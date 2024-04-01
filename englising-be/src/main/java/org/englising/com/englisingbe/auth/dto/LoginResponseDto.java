package org.englising.com.englisingbe.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

// todo. 안쓰면 삭제
@Builder
@Data
@AllArgsConstructor
public class LoginResponseDto {
    public Long userId;
}
