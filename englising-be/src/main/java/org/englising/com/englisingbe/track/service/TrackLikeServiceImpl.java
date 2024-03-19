package org.englising.com.englisingbe.track.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.track.repository.TrackLikeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackLikeServiceImpl {
    private final TrackLikeRepository trackLikeRepository;

    public List<Long> getLikedTrackIdsByUserId(Long userId){
        return trackLikeRepository.findBytrack;
    }
}
