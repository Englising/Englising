package org.englising.com.englisingbe.singleplay.repository;

import jakarta.transaction.Transactional;
import org.englising.com.englisingbe.singleplay.entity.SinglePlay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SinglePlayRepository extends JpaRepository<SinglePlay, Long> {
    @Query("SELECT DISTINCT sp FROM SinglePlay sp WHERE sp.user.userId = :userId GROUP BY sp.track.trackId")
    Page<SinglePlay> getSinglePlayByUserUserId(Long userId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE SinglePlay sp SET sp.score = :score, sp.correctRate = :correctRate WHERE sp.singlePlayId = :singlePlayId")
    void updateScoreAndCorrectRateById(Long singlePlayId, Integer score, Integer correctRate);


    @Query(value = "SELECT MAX(sp.score) FROM singleplay sp WHERE sp.user_id = :userId AND sp.track_id = :trackId GROUP BY sp.singleplay_level_id", nativeQuery = true)
    List<Integer> findMaxScoresByUserIdAndTrackIdGroupedByLevel(@Param("userId") Long userId, @Param("trackId") Long trackId);
}
