package org.englising.com.englisingbe.track.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.singleplay.dto.TrackWithArtistDto;
import org.englising.com.englisingbe.singleplay.dto.response.TrackResponseDto;
import org.englising.com.englisingbe.track.repository.TrackLikeRepository;
import org.englising.com.englisingbe.track.repository.TrackLikeRepositorySupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TrackLikeServiceImpl {
    private final TrackLikeRepository trackLikeRepository;
    private final TrackLikeRepositorySupport trackLikelRepositorySupport;

    public List<TrackResponseDto> getLikedTrackResponseDtoByUserId(Long userId, Pageable pageable) {
        Page<TrackWithArtistDto> trackResponseDtoPage = trackLikelRepositorySupport.findLikedTracksWithAlbumAndArtistByUserId(userId, pageable);
        return trackResponseDtoPage.getContent().stream().map(trackWithArtistDto -> {
            return TrackResponseDto.getTrackResponseDtoFromTrack(trackWithArtistDto);
        }).collect(Collectors.toList());
    }
}
