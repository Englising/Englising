package org.englising.com.englisingbe.multiplay.service;

import java.util.List;

import org.englising.com.englisingbe.global.util.Genre;
import org.englising.com.englisingbe.multiplay.dto.request.MultiPlayRequestDto;
import org.englising.com.englisingbe.multiplay.dto.response.MultiPlayDetailResponseDto;
import org.englising.com.englisingbe.multiplay.dto.response.MultiPlayListResponseDto;
import org.englising.com.englisingbe.multiplay.entity.MultiPlay;

public interface MultiPlayService {
    MultiPlay createMultiPlay(MultiPlayRequestDto requestDto);
    MultiPlayDetailResponseDto getMultiPlayById(Long multiPlayId);
    List<MultiPlayListResponseDto> getMultiPlayList(String genre, Integer page, Integer size);

    List<MultiPlayListResponseDto> getMultiPlayList(Genre genre, Integer page, Integer size);

    Boolean getMultiPlayResult(Long multiplayId);
}
