package org.englising.com.englisingbe.multiplay.repository;

import org.englising.com.englisingbe.multiplay.entity.MultiPlayHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MultiPlayHintRepository extends JpaRepository<MultiPlayHint, Long> {
    MultiPlayHint findByMultiplayHintId(Long multiPlayHintId);

    @Query(value = "SELECT * FROM multiplay_hint ORDER BY RAND() LIMIT 1", nativeQuery = true)
    MultiPlayHint findRandom();
}
