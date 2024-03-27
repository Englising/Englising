package org.englising.com.englisingbe.multiplay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.englising.com.englisingbe.global.util.Genre;

@Data
@Builder
@AllArgsConstructor
public class MultiPlayListResponseDto {
    private Long multiPlayId;
    private String roomName;
    private Integer currentUser;
    private Integer maxUser;
    private String multiPlayImgUrl;
    private Genre genre;
    private Boolean isSecret;
    private Integer password;
}
