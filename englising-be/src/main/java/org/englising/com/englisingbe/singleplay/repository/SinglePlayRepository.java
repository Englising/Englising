package org.englising.com.englisingbe.singleplay.repository;

import org.englising.com.englisingbe.singleplay.entity.SinglePlay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SinglePlayRepository extends JpaRepository<SinglePlay, Long> {
    Page<SinglePlay> getSinglePlayByUserUserId(Long userId, Pageable pageable);
}
