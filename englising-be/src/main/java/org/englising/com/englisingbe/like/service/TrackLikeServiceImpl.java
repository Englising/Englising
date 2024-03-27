package org.englising.com.englisingbe.like.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.englising.com.englisingbe.like.dto.TrackLikeResponseDto;
import org.englising.com.englisingbe.like.entity.TrackLike;
import org.englising.com.englisingbe.like.repository.TrackLikeRepository;
import org.englising.com.englisingbe.like.repository.TrackLikeRepositorySupport;
import org.englising.com.englisingbe.music.entity.Track;
import org.englising.com.englisingbe.music.service.TrackServiceImpl;
import org.englising.com.englisingbe.user.entity.User;
import org.englising.com.englisingbe.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TrackLikeServiceImpl {

    private final TrackLikeRepository trackLikeRepository;
    private final TrackLikeRepositorySupport trackLikeRepositorySupport;
    private final TrackServiceImpl trackService;
    private final UserService userService;

    public Page<TrackLike> getLikedTrackResponseDtoByUserId(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt", "createdAt"));
        return trackLikeRepository.getTrackLikeByUserUserIdAndIsLikedTrue(userId, pageable);
    }

    public boolean checkTrackLikeByUserId(Long userId, Long trackId){
        return trackLikeRepository.existsByUserUserIdAndTrackTrackIdAndIsLikedTrue(userId, trackId);
    }

    public TrackLikeResponseDto likeTrack(Long trackId, Long userId) {
        TrackLike trackLike = trackLikeRepository.findByTrackIdAndUserId(userId, trackId)
                .orElse(null);

        // 해당 테이블에 존재하지 않으면 좋아요 처음인 것
        if (trackLike == null) {
            User user = userService.getUserById(userId);
            Track track = trackService.getTrackByTrackId(trackId);

            TrackLike savedTrackLike = TrackLike.builder()
                    .track(track)
                    .user(user)
                    .isLiked(true)
                    .build();
            trackLikeRepository.save(savedTrackLike);

            return TrackLikeResponseDto.builder()
                    .liked(savedTrackLike.getIsLiked())
                    .build();
        }

        boolean updatedLikeStatus = !trackLike.getIsLiked();
        trackLike.updateTrackLike(updatedLikeStatus);

        return TrackLikeResponseDto.builder()
                .liked(trackLike.getIsLiked())
                .build();
    }
}
