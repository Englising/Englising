package org.englising.com.englisingbe.word.repository;

import org.englising.com.englisingbe.word.entity.Word;
import org.englising.com.englisingbe.word.entity.WordLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {

}
