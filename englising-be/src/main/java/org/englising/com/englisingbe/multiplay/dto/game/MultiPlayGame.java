package org.englising.com.englisingbe.multiplay.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.englising.com.englisingbe.global.util.Genre;
import org.englising.com.englisingbe.global.util.MultiPlayStatus;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
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
    private int selectedHint;

    private Long managerUserId;
    private List<MultiPlayUser> users;
    private int round;
    private MultiPlayStatus status;
}
