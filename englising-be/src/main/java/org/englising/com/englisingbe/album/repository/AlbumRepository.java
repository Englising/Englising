package org.englising.com.englisingbe.album.repository;

import org.englising.com.englisingbe.album.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Long, Album> {
}
