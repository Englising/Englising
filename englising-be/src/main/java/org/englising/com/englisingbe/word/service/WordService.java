package org.englising.com.englisingbe.word.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.englising.com.englisingbe.user.entity.User;
import org.englising.com.englisingbe.user.repository.UserRepository;
import org.englising.com.englisingbe.user.service.UserService;
import org.englising.com.englisingbe.word.dto.WordLikeResponseDto;
import org.englising.com.englisingbe.word.dto.WordListResponseDto;
import org.englising.com.englisingbe.word.dto.WordListType;
import org.englising.com.englisingbe.word.entity.Word;
import org.englising.com.englisingbe.word.entity.WordLike;
import org.englising.com.englisingbe.word.repository.WordLikeRepository;
import org.englising.com.englisingbe.word.repository.WordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class WordService {

    private final UserService userService;
    private final WordRepository wordRepository;
    private final WordLikeRepository wordLikeRepository;

    public Word getWordById(long wordId) {
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new GlobalException(ErrorHttpStatus.WORD_NOT_FOUND));

        return word;
    }

    //todo.
    public WordListResponseDto getWordList(WordListType type, Integer page, Integer size, String userId) {
     return null;
    }

    public WordLikeResponseDto likeWord(Long wordId, String userId) {
        WordLike wordLike = wordLikeRepository.findByWordIdAndUserId(wordId, Long.valueOf(userId)).orElse(null);

        if (wordLike == null) {
            User user = userService.getUserById(Long.parseLong(userId));
            Word word = getWordById(wordId);

            WordLike savedWordlike = WordLike.builder()
                    .isLiked(true)
                    .user(user)
                    .word(word)
                    .build();
            wordLikeRepository.save(savedWordlike);
            return new WordLikeResponseDto(savedWordlike.getIsLiked());
        }

        boolean updatedLikeStatus = !wordLike.getIsLiked();
        wordLike.updateWordLike(updatedLikeStatus);

        return new WordLikeResponseDto(wordLike.getIsLiked());
    }
}
