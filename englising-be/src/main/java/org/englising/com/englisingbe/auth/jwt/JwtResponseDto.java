package org.englising.com.englisingbe.auth.jwt;

import lombok.*;

@Data
@AllArgsConstructor
public class JwtResponseDto {
    Long userId;
    String accessToken;
    String refreshToken;
}
