package org.englising.com.englisingbe.multiplay.repository;

import java.util.List;

import org.englising.com.englisingbe.global.util.Genre;
import org.englising.com.englisingbe.multiplay.entity.MultiPlay;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MultiPlayRepository extends JpaRepository<MultiPlay, Long> {
    MultiPlay findByMultiplayId(Long multiPlayId);
    List<MultiPlay> findByGenre(Genre genre, PageRequest of);
//    MultiPlay save(MultiPlay multiPlay);
    List<MultiPlay> findAll();
}
