package org.englising.com.englisingbe.music.repository;

import org.englising.com.englisingbe.music.entity.Lyric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface LyricRepository extends JpaRepository<Lyric, Long> {
    Optional<List<Lyric>> findAllByTrackTrackId(Long trackId);
    Optional<Lyric> findByTrackTrackIdAndStartTime(Long trackId, BigDecimal startTime);

}
