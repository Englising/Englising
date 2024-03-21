package org.englising.com.englisingbe.multiplay.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultiPlayDetailResponseDto {
    public Long trackId;
    public Long multiplayId;
    public String roomName;
    public Integer maxUser;
    public String genre;
    public Boolean isSecret;
    public Integer roomPw;
    public String multiPlayImgUrl;
}
