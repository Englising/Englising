package org.englising.com.englisingbe.multiplay.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.englising.com.englisingbe.global.util.MultiPlayUserRole;


@Data
@Builder
@AllArgsConstructor
public class MultiPlayUser {
    private Long userId;
    private String nickname;
    private String profileImg;
    private String color;
    private MultiPlayUserRole userRole;
}
