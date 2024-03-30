package org.englising.com.englisingbe.music.repository;

import org.englising.com.englisingbe.music.entity.Lyric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LyricRepository extends JpaRepository<Lyric, Long> {
    Optional<List<Lyric>> findAllByTrackTrackId(Long trackId);
}
