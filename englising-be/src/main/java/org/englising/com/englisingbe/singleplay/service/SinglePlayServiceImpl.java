package org.englising.com.englisingbe.singleplay.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.global.util.PlayListType;
import org.englising.com.englisingbe.singleplay.dto.response.TrackResponseDto;
import org.englising.com.englisingbe.track.service.TrackLikeServiceImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SinglePlayServiceImpl {
    private final TrackLikeServiceImpl trackLikeService;

    public List<TrackResponseDto> getPlayList(PlayListType type, Integer page, Integer size, Long userId) {
        Pageable pageable = PageRequest.of(page, size);
        switch (type) {
            case like -> {
                return trackLikeService.getLikedTrackResponseDtoByUserId(userId, pageable);
            }
            case recent -> {
                // 유저가 플레이한 목록 최신 순으로 조회 로직
                // PlayRepository 등을 사용한 로직 구현
                return null;
            }
            case recommend -> {
                // FastAPI를 통해 추천 플레이리스트 가져오는 로직 구현
                // 예: FastAPIClient.getRecommendations(userId);
                return null;
            }
        }
        return null;
    }
}
