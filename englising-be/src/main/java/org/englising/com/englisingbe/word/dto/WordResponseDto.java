package org.englising.com.englisingbe.word.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordResponseDto {
    public Long wordId;
    public String enText;
    public String koText;
    public String example;
    public boolean isLiked;
}
