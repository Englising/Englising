package org.englising.com.englisingbe.word.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.englising.com.englisingbe.global.dto.PaginationDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordListResponseDto {
    public List<WordResponseDto> wordResponseDto;
    public PaginationDto pagination;
}
