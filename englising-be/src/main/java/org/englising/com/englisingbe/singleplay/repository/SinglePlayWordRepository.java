package org.englising.com.englisingbe.singleplay.repository;

import org.englising.com.englisingbe.singleplay.entity.SinglePlay;
import org.englising.com.englisingbe.singleplay.entity.SinglePlayWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SinglePlayWordRepository extends JpaRepository<SinglePlayWord, Long> {
    Optional<List<SinglePlayWord>> getAllBySinglePlaySinglePlayId(long singlePlayId);
    List<SinglePlayWord> findBySinglePlaySinglePlayIdIn(List<Long> singlePlayIds);

}
