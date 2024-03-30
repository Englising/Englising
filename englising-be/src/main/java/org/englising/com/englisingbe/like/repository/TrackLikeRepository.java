package org.englising.com.englisingbe.like.repository;

import org.englising.com.englisingbe.like.entity.TrackLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackLikeRepository extends JpaRepository<TrackLike, Long> {
    Page<TrackLike> getTrackLikeByUserUserId(Long userId, Pageable pageable);
    boolean existsByUserUserIdAndTrackTrackIdAndIsLikedTrue(Long userId, Long trackId);
}