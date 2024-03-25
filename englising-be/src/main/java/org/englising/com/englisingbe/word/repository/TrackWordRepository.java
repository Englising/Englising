package org.englising.com.englisingbe.word.repository;

import org.englising.com.englisingbe.word.entity.TrackWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackWordRepository extends JpaRepository<TrackWord, Long> {
    List<TrackWord> findTrackWordByTrackTrackId(Long trackId);
}
