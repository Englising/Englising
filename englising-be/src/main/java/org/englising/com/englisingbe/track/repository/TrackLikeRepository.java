package org.englising.com.englisingbe.track.repository;

import org.englising.com.englisingbe.track.entity.TrackLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrackLikeRepository extends JpaRepository<TrackLike, Long> {
    Page<TrackLike> getTrackLikeByUserUserId(Long userId, Pageable pageable);
}