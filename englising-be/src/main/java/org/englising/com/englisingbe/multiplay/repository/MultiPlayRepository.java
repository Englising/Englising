package org.englising.com.englisingbe.multiplay.repository;

import java.util.List;
import org.englising.com.englisingbe.multiplay.entity.MultiPlay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MultiPlayRepository extends JpaRepository<Long, MultiPlay> {
    MultiPlay findByMultiplayId(Long multiPlayId);
    MultiPlay save(MultiPlay multiPlay);


}
