package org.englising.com.englisingbe.singleplay.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.global.dto.PaginationDto;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.englising.com.englisingbe.global.util.PlayListType;
import org.englising.com.englisingbe.singleplay.dto.response.PlayListDto;
import org.englising.com.englisingbe.singleplay.dto.response.TrackResponseDto;
import org.englising.com.englisingbe.singleplay.entity.SinglePlay;
import org.englising.com.englisingbe.singleplay.entity.SinglePlayHint;
import org.englising.com.englisingbe.singleplay.repository.SinglePlayHintRepository;
import org.englising.com.englisingbe.singleplay.repository.SinglePlayRepository;
import org.englising.com.englisingbe.music.dto.TrackAlbumArtistDto;
import org.englising.com.englisingbe.like.entity.TrackLike;
import org.englising.com.englisingbe.like.service.TrackLikeServiceImpl;
import org.englising.com.englisingbe.music.service.TrackServiceImpl;
import org.englising.com.englisingbe.user.service.UserService;
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
    private final UserService userService;

    private final SinglePlayRepository singlePlayRepository;
    private final SinglePlayHintRepository singlePlayHintRepository;

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

    public void createSinglePlay(Long userId, Long trackId, Integer singlePlayLevelId){
        // TODO
        // SinglePlay Repository에 Create
        // sigleplay-word에 추가
        SinglePlay singlePlay = singlePlayRepository.save(SinglePlay.builder()
                        .singlePlayHint(getSinglePlayHintById(singlePlayLevelId))
                        .user(userService.getUserById(userId))
                        .track(trackService.getTrackByTrackId(trackId))
                .build());


    }

    public void checkWord(){

    }

    public void getLyricStartTimes(){

    }

    private SinglePlayHint getSinglePlayHintById(Integer singlePlayHintId){
        return singlePlayHintRepository.findSinglePlayHintBySingleplayLevelId(singlePlayHintId)
                .orElseThrow(()-> new GlobalException(ErrorHttpStatus.NO_MATCHING_HINT));
    }

    private PlayListDto getLikedTracks(Integer page, Integer size, Long userId){
        Page<TrackLike> trackLikes = trackLikeService.getLikedTrackResponseDtoByUserId(userId, page, size);
        List<TrackAlbumArtistDto> tracks = trackService.getTrackAlbumArtistsByTrackIds(trackLikes.getContent()
                .stream()
                .map( trackLike -> {
                            return trackLike.getTrack().getTrackId();
                        }
                ).collect(Collectors.toList()));
        return getPlayListDtoFromPageAndList(getTrackResponseDtoFromTrackAlbumArtist(userId, tracks),trackLikes);
    }

    private PlayListDto getRecentTracks(Integer page, Integer size, Long userId){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<SinglePlay> singlePlays = singlePlayRepository.getSinglePlayByUserUserId(userId, pageable);
        List<TrackAlbumArtistDto> tracks = trackService.getTrackAlbumArtistsByTrackIds(singlePlays
                .stream()
                .map(singlePlay -> {
                    return singlePlay.getTrack().getTrackId();
        }).collect(Collectors.toList()));
        return getPlayListDtoFromPageAndList(getTrackResponseDtoFromTrackAlbumArtist(userId,tracks), singlePlays);
    }

    private PlayListDto getRecommendedTracks(Integer page, Integer size, Long userId){
        // TODO FastAPI를 통해 추천 플레이리스트 가져오는 로직 구현
        return getLikedTracks(page, size, userId);
    }

    private List<TrackResponseDto> getTrackResponseDtoFromTrackAlbumArtist(Long userId, List<TrackAlbumArtistDto> trackAlbumArtistDtos){
        return trackAlbumArtistDtos
                .stream()
                .map(trackAlbumArtistDto -> {
                    return TrackResponseDto.getTrackResponseFromTrackAlbumArtist(
                            trackAlbumArtistDto,
                            0,
                            trackLikeService.checkTrackLikeByUserId(userId, trackAlbumArtistDto.getTrack().getTrackId())
                    );
                }).toList();
    }

    private PlayListDto getPlayListDtoFromPageAndList(List<TrackResponseDto> data, Page<?> page){
        return PlayListDto.builder()
                .playList(data)
                .pagination(PaginationDto.from(page))
                .build();
    }
}
