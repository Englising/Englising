package org.englising.com.englisingbe.multiplay.repository;

import java.util.List;
import org.englising.com.englisingbe.multiplay.entity.MultiPlay;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MultiPlayRepository extends JpaRepository<MultiPlay, Long> {
    MultiPlay findByMultiplayId(Long multiPlayId);
    List<MultiPlay> findByGenre(String genre, PageRequest of);
//    MultiPlay save(MultiPlay multiPlay);
    List<MultiPlay> findAll();
}
