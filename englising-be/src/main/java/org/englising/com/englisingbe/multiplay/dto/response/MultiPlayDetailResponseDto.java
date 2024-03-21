package org.englising.com.englisingbe.multiplay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.englising.com.englisingbe.global.util.Genre;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlayUser;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MultiPlayDetailResponseDto {
    private Long multiPlayId;
    private String roomName;
    private String multiPlayImgUrl;
    private Genre genre;
    public Long trackId;
    private List<MultiPlayUser> currentUser;
    private Integer maxUser;
    private boolean isSecret;
}
