package org.englising.com.englisingbe.artist.repository;

import org.englising.com.englisingbe.artist.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
