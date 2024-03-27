package org.englising.com.englisingbe.like.repository;

import org.englising.com.englisingbe.like.entity.TrackLike;
import org.englising.com.englisingbe.word.entity.WordLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TrackLikeRepository extends JpaRepository<TrackLike, Long> {
    Page<TrackLike> getTrackLikeByUserUserIdAndIsLikedTrue(Long userId, Pageable pageable);
    boolean existsByUserUserIdAndTrackTrackIdAndIsLikedTrue(Long userId, Long trackId);
    @Query(value = "SELECT * FROM track_like t WHERE t.track_id = :trackId and t.user_id = :userId", nativeQuery = true)
    Optional<TrackLike> findByTrackIdAndUserId(Long userId, Long trackId);


}