package org.englising.com.englisingbe.multiplay.repository;

import org.englising.com.englisingbe.multiplay.entity.MultiPlayHint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MultiPlayHintRepository extends JpaRepository<MultiPlayHint, Long> {
    MultiPlayHint findByMultiplayHintId(Long multiPlayHintId);
}
