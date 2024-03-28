package org.englising.com.englisingbe.multiplay.dto.socket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlayUser;

@Data
@Builder
@AllArgsConstructor
public class ParticipantDto {
    private String kind;
    private MultiPlayUser user;
}
