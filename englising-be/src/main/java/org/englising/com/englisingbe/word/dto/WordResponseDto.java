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
    public String korText1;
    public String korText2;
    public String korText3;
    public String example;
    public boolean isLiked;

//    public static WordResponseDto getWordResponseDto()








}
