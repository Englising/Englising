package org.englising.com.englisingbe.multiplay.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.englising.com.englisingbe.global.util.Genre;
import org.englising.com.englisingbe.global.util.MultiPlayStatus;
import org.englising.com.englisingbe.user.entity.User;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MultiPlayGame {
    private Long multiPlayId;
    private Long trackId;
    private int maxUser;
    private String roomName;
    private Genre genre;
    private boolean isSecret;
    private int roomPw;
    private String multiplayImgUrl;

    private List<MultiPlaySentence> sentences;

    private List<User> users;
    private int round;
    private MultiPlayStatus status;
    private int selectedHint;
}