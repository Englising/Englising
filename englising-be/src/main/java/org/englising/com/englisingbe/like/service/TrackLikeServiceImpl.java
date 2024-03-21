package org.englising.com.englisingbe.like.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.like.entity.TrackLike;
import org.englising.com.englisingbe.like.repository.TrackLikeRepository;
import org.englising.com.englisingbe.like.repository.TrackLikeRepositorySupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TrackLikeServiceImpl {
    private final TrackLikeRepository trackLikeRepository;
    private final TrackLikeRepositorySupport trackLikeRepositorySupport;

    public Page<TrackLike> getLikedTrackResponseDtoByUserId(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt", "createdAt"));
        return trackLikeRepository.getTrackLikeByUserUserId(userId, pageable);
    }

    public boolean checkTrackLikeByUserId(Long userId, Long trackId){
        return trackLikeRepository.existsByUserUserIdAndTrackTrackIdAndIsLikedTrue(userId, trackId);
    }
}
