package org.englising.com.englisingbe.music.repository;

import org.englising.com.englisingbe.music.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {
}
