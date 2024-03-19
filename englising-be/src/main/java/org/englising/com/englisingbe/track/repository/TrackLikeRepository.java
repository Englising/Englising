package org.englising.com.englisingbe.track.repository;

import org.englising.com.englisingbe.track.entity.TrackLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TrackLikeRepository extends JpaRepository<TrackLike, Long> {
    @Query("select tl.track.trackId from TrackLike tl where tl.user.userId = :userId and tl.isLiked = true order by tl.updatedAt DESC, tl.createdAt DESC")
    Page<Long> findLikedTrackIdsByUserIdOrdered(Long userId, Pageable pageable);
}