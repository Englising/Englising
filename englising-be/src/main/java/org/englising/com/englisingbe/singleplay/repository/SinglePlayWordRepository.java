package org.englising.com.englisingbe.singleplay.repository;

import org.englising.com.englisingbe.singleplay.entity.SinglePlayWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SinglePlayWordRepository extends JpaRepository<SinglePlayWord, Long> {
    Optional<List<SinglePlayWord>> getAllBySinglePlaySinglePlayId(long singlePlayId);
}
