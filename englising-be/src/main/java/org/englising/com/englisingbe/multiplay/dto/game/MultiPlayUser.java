package org.englising.com.englisingbe.multiplay.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.englising.com.englisingbe.user.entity.User;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultiPlayUser {
    private Long userId;
    private String nickname;
    private String profileImg;
    private String color;
    public static MultiPlayUser getMultiPlayUserFromUser(User user){
        return MultiPlayUser.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImg())
                .color(user.getColor())
                .build();
    }
}
