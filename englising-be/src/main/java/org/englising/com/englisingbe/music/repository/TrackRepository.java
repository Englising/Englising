package org.englising.com.englisingbe.music.repository;

import org.englising.com.englisingbe.music.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {
}
