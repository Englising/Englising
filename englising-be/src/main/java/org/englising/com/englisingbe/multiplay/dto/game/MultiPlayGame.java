package org.englising.com.englisingbe.multiplay.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.englising.com.englisingbe.global.util.Genre;
import org.englising.com.englisingbe.global.util.MultiPlayStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.englising.com.englisingbe.music.entity.Lyric;
import org.englising.com.englisingbe.music.entity.Track;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultiPlayGame {
    private Long multiPlayId;
    private Track track;
    private int maxUser;
    private String roomName;
    private Genre genre;
    private boolean isSecret;
    private int roomPw;
    private String multiplayImgUrl;

    private String beforeLyric;
    private BigDecimal beforeLyricStartTime;
    private String afterLyric;
    private BigDecimal afterLyricEndTime;

    private List<MultiPlaySentence> sentences;
    private Map<Integer, String> answerAlphabets;
    private int selectedHint;

    private Long managerUserId;
    private List<MultiPlayUser> users;
    private int round;
    private MultiPlayStatus status;

    private boolean isGameEnd;
}
