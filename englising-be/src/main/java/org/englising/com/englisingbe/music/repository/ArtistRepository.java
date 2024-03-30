package org.englising.com.englisingbe.music.repository;

import org.englising.com.englisingbe.music.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
