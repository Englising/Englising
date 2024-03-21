package org.englising.com.englisingbe.multiplay.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.englising.com.englisingbe.multiplay.dto.request.MultiPlayRequestDto;
import org.englising.com.englisingbe.multiplay.dto.response.MultiPlayListResponseDto;
import org.englising.com.englisingbe.multiplay.entity.MultiPlay;
import org.englising.com.englisingbe.multiplay.repository.MultiPlayRepository;
import org.englising.com.englisingbe.track.dto.TrackAlbumArtistDto;
import org.englising.com.englisingbe.track.entity.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
        multiPlay.setRoomName(requestDto.getRoomName());
        multiPlay.setIsSecret(requestDto.isSecret);
        multiPlay.setRoomPw(requestDto.roomPw);
        multiPlay.setMaxUser(requestDto.maxUser);
        // 랜덤한 trackId 생성
        Random random = new Random();
        long randomTrackId = random.nextInt(1000); // 예를 들어 1000 이내의 랜덤한 값 생성
        Track track = new Track();
        multiPlay.setTrackId(Track.builder().trackId(randomTrackId).build());
//        multiPlay.setMultiImg("default_image.jpg"); // 기본 이미지 설정 등
        return multiPlayRepository.save(multiPlay);
    }

    @Override
    public MultiPlay getMultiPlayById(Long multiPlayId) {
        return multiPlayRepository.findByMultiplayId(multiPlayId);
    }

    @Override
    public List<MultiPlayListResponseDto> getMultiPlayList(String genre, Integer page,
        Integer size) {
        List<MultiPlay> multiPlays = multiPlayRepository.findByGenre(genre, PageRequest.of(page, size));
        List<MultiPlayListResponseDto> multiPlayListResponseDto = new ArrayList<>();
        for (MultiPlay multiPlay : multiPlays) {
            MultiPlayListResponseDto dto = new MultiPlayListResponseDto();
            dto.setMultiplayId(multiPlay.getMultiplayId());
            dto.setRoomName(multiPlay.getRoomName());
//            dto.setCurrentUser(multiPlay.getCurrentUser());
            dto.setMaxUser(multiPlay.getMaxUser());
//            dto.setMultiImg(multiPlay.getMultiImg());
            multiPlayListResponseDto.add(dto);
        }
        return multiPlayListResponseDto;
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