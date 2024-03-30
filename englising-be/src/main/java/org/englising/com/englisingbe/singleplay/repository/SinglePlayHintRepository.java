package org.englising.com.englisingbe.singleplay.repository;

import org.englising.com.englisingbe.singleplay.entity.SinglePlayHint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SinglePlayHintRepository extends JpaRepository<SinglePlayHint, Long> {
    Optional<SinglePlayHint> findSinglePlayHintBySingleplayLevelId(int singlePlayLevelId);
}
