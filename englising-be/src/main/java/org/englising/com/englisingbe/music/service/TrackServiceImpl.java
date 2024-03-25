package org.englising.com.englisingbe.music.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.englising.com.englisingbe.music.dto.TrackAlbumArtistDto;
import org.englising.com.englisingbe.music.entity.Track;
import org.englising.com.englisingbe.music.repository.TrackRepository;
import org.englising.com.englisingbe.music.repository.TrackRepositorySupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrackServiceImpl {
    private final TrackRepository trackRepository;
    private final TrackRepositorySupport trackRepositorySupport;

    public List<TrackAlbumArtistDto> getTrackAlbumArtistsByTrackIds(List<Long> trackIds) {
        return trackIds.stream()
                .map(trackId -> trackRepositorySupport.findTrackWithAlbumAndArtistsByTrackId(trackId)
                        .orElseThrow(() -> new GlobalException(ErrorHttpStatus.NO_MATCHING_TRACK)))
                .collect(Collectors.toList());
    }

    public TrackAlbumArtistDto getTrackAlbumArtistByTrackId(Long trackId) {
        return trackRepositorySupport.findTrackWithAlbumAndArtistsByTrackId(trackId)
                .orElseThrow(() -> new GlobalException(ErrorHttpStatus.NO_MATCHING_TRACK));
    }

    public Track getTrackByTrackId(Long trackId){
        return trackRepository.findById(trackId)
                .orElseThrow(() -> new GlobalException(ErrorHttpStatus.NO_MATCHING_TRACK));
    }

    public Page<Track> getSearchTrackIds(String keyword, Pageable pageable){
        return trackRepository.findByTitleContainingAndYoutubeIdIsNotNull(keyword, pageable);
    }

    public Track getRandomTrack(){
        return trackRepository.findRandomTrackWithLyricsAndYoutubeId();
    }
}
