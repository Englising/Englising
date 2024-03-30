package org.englising.com.englisingbe.word.repository;

import org.englising.com.englisingbe.word.entity.WordLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WordLikeRepository extends JpaRepository<WordLike, Long> {

    @Query(value = "SELECT * FROM word_like w WHERE w.word_id = :wordId and w.user_id = :userId", nativeQuery = true)
    Optional<WordLike> findByWordIdAndUserId(Long wordId, Long userId);

    Page<WordLike> getWordLikeByUserUserId(Long userId, Pageable pageable);

    Page<WordLike> getWordLikeByUserUserIdAndIsLikedTrue(Long userId, Pageable pageable);

    boolean existsByUserUserIdAndWordWordIdAndIsLikedTrue(Long userId, Long wordId);
}
