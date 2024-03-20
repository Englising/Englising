package org.englising.com.englisingbe.singleplay.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.global.dto.PaginationDto;
import org.englising.com.englisingbe.global.util.PlayListType;
import org.englising.com.englisingbe.singleplay.dto.response.PlayListDto;
import org.englising.com.englisingbe.singleplay.dto.response.TrackResponseDto;
import org.englising.com.englisingbe.track.dto.TrackAlbumArtistDto;
import org.englising.com.englisingbe.track.entity.TrackLike;
import org.englising.com.englisingbe.track.service.TrackLikeServiceImpl;
import org.englising.com.englisingbe.track.service.TrackServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SinglePlayServiceImpl {
    private final TrackLikeServiceImpl trackLikeService;
    private final TrackServiceImpl trackService;

    public PlayListDto getPlayList(PlayListType type, Integer page, Integer size, Long userId) {
        switch (type) {
            case like -> {
                return getLikedTracks(page, size, userId);
            }
            case recent -> {
                return getRecentTracks(page, size, userId);
            }
            case recommend -> {
                return getRecommendedTracks(page, size, userId);
            }
        }
        return null;
    }

    private PlayListDto getLikedTracks(Integer page, Integer size, Long userId){
        Page<TrackLike> trackLikes = trackLikeService.getLikedTrackResponseDtoByUserId(userId, page, size);
        List<TrackAlbumArtistDto> tracks = trackService.getTracksByTrackIds(trackLikes.getContent()
                .stream()
                .map( trackLike -> {
                            return trackLike.getTrack().getTrackId();
                        }
                ).collect(Collectors.toList()));
        List<TrackResponseDto> trackResponseDtoList = tracks.stream().map(trackAlbumArtistDto -> {
            return TrackResponseDto.getTrackResponseFromTrackAlbumArtist(trackAlbumArtistDto, 0, true);
        }).collect(Collectors.toList());
        return PlayListDto.builder()
                .playList(trackResponseDtoList)
                .pagination(PaginationDto.from(trackLikes))
                .build();
    }

    private PlayListDto getRecentTracks(Integer page, Integer size, Long userId){
        // 유저가 플레이한 목록 최신 순으로 조회 로직
        // PlayRepository 등을 사용한 로직 구현
        return null;
    }

    private PlayListDto getRecommendedTracks(Integer page, Integer size, Long userId){
        // FastAPI를 통해 추천 플레이리스트 가져오는 로직 구현
        // 예: FastAPIClient.getRecommendations(userId);
        return null;
    }
}
