package org.englising.com.englisingbe.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ProfileDto {
    @JsonProperty("profile_img")
    String profileImg;
    String color;
    String nickname;
}
