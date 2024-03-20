package org.englising.com.englisingbe.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ProfileDto {
    String profileImg;
    String nickname;

}
