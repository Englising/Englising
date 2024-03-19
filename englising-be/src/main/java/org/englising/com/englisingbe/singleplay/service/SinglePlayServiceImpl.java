package org.englising.com.englisingbe.singleplay.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.album.repository.AlbumRepository;
import org.englising.com.englisingbe.global.util.PlayListType;
import org.englising.com.englisingbe.singleplay.dto.response.PlayListResponseDto;
import org.englising.com.englisingbe.track.entity.Track;
import org.englising.com.englisingbe.track.repository.ArtistTrackRepository;
import org.englising.com.englisingbe.track.repository.TrackLikeRepository;
import org.englising.com.englisingbe.track.repository.TrackRepository;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SinglePlayServiceImpl {

    private final TrackLikeRepository likeRepository;
    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;
    private final ArtistTrackRepository artistTrackRepository;

    public List<PlayListResponseDto> getPlayList(PlayListType type, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        switch (type) {
            case like -> {
                // LikeRepository에서 사용자가 좋아한 목록 최신 순으로 조회
                Page<Long> likedTrackIds = likeRepository.findLikedTrackIdsByUserIdOrdered(userId, pageable);

                // TrackRepository와 AlbumRepository, ArtistTrackRepository를 사용하여 필요한 정보 가져오기
                List<Track> tracks = trackRepository.findAllById(likedTrackIds.getContent());
                // 앨범과 아티스트 정보도 마찬가지로 조회

                // 조회한 정보를 기반으로 PlayListResponseDto 리스트 생성 및 반환
                return tracks.stream()
                        .map(track -> new PlayListResponseDto(track, /* 앨범 정보 */, /* 아티스트 정보 */))
                        .collect(Collectors.toList());
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
            default -> return null;
        }
    }
}
