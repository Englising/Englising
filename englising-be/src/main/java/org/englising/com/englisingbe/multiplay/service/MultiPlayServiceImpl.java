package org.englising.com.englisingbe.multiplay.service;

import java.util.List;
import org.englising.com.englisingbe.multiplay.dto.request.MultiPlayRequestDto;
import org.englising.com.englisingbe.multiplay.dto.response.MultiPlayListResponseDto;
import org.englising.com.englisingbe.multiplay.entity.MultiPlay;
import org.englising.com.englisingbe.multiplay.repository.MultiPlayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MultiPlayServiceImpl implements MultiPlayService {
    private final MultiPlayRepository multiPlayRepository;

    @Autowired
    public MultiPlayServiceImpl(MultiPlayRepository multiPlayRepository) {
        this.multiPlayRepository = multiPlayRepository;
    }

    @Override
    public MultiPlay createMultiPlay(MultiPlayRequestDto requestDto) {
        MultiPlay multiPlay = new MultiPlay();
        multiPlay.setGenre(requestDto.getGenre());
        multiPlay.setRoomName("방 이름");
        multiPlay.setMaxUser(6);
        multiPlay.setCurrentUser(0);
        multiPlay.setMultiImg("default_image.jpg"); // 기본 이미지 설정 등
        return multiPlayRepository.save(multiPlay);
    }

    @Override
    public MultiPlay getMultiPlayById(Long multiPlayId) {
        return multiPlayRepository.findByMultiplayId(multiPlayId);
    }

    @Override
    public List<MultiPlayListResponseDto> getMultiPlayList(String genre, Integer page,
        Integer size) {
        return null;
    }

    @Override
    public Boolean getMultiPlayResult(Long multiplayId) {
        return multiPlayRepository.findByMultiplayId(multiplayId).getIsSecret();
    }

    @Override
    public List<Long> getMultiPlayList() {
        return null;
    }
}