package org.englising.com.englisingbe.track.repository;

import org.englising.com.englisingbe.track.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {
}
