package org.englising.com.englisingbe.multiplay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.englising.com.englisingbe.global.util.Genre;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultiPlayRequestDto {
    public Genre genre;
    public String roomName;
    public int maxUser;
    public boolean isSecret;
    public int roomPw;
    public String multiPlayImgUrl;
}
