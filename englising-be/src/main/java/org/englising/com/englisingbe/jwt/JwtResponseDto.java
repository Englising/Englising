package org.englising.com.englisingbe.jwt;


import lombok.*;


@Data
@AllArgsConstructor
public class JwtResponseDto {
    String accessToken;
    String refreshToken;

}
