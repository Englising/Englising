package org.englising.com.englisingbe.multiplay.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MultiPlayWord {
    private Integer wordIndex;
    private List<MultiPlayAlphabet> word;
}
