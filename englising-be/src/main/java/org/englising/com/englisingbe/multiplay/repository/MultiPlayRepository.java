package org.englising.com.englisingbe.multiplay.repository;

import org.englising.com.englisingbe.multiplay.entity.MultiPlay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MultiPlayRepository extends JpaRepository<MultiPlay, Long> {
    MultiPlay findByMultiplayId(Long multiPlayId);
//    MultiPlay save(MultiPlay multiPlay);
//    List<MultiPlay> findAll();
}
