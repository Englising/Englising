package org.englising.com.englisingbe.multiplay.service;

import java.util.List;
import org.englising.com.englisingbe.multiplay.dto.request.MultiPlayRequestDto;
import org.englising.com.englisingbe.multiplay.dto.response.MultiPlayListResponseDto;
import org.englising.com.englisingbe.multiplay.entity.MultiPlay;

public interface MultiPlayService {
    List<MultiPlayListResponseDto> getMultiPlayList(int page, int size);
    MultiPlay createMultiPlay(MultiPlayRequestDto multiPlayRequestDto);
    MultiPlay getMultiPlayById(Long multiPlayId);


}
