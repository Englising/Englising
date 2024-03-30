package org.englising.com.englisingbe.word.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.englising.com.englisingbe.global.dto.PaginationDto;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.englising.com.englisingbe.singleplay.entity.SinglePlay;
import org.englising.com.englisingbe.singleplay.entity.SinglePlayWord;
import org.englising.com.englisingbe.singleplay.repository.SinglePlayRepository;
import org.englising.com.englisingbe.singleplay.repository.SinglePlayWordRepository;
import org.englising.com.englisingbe.user.entity.User;
import org.englising.com.englisingbe.user.service.UserService;
import org.englising.com.englisingbe.word.dto.WordLikeResponseDto;
import org.englising.com.englisingbe.word.dto.WordListResponseDto;
import org.englising.com.englisingbe.word.dto.WordListType;
import org.englising.com.englisingbe.word.dto.WordResponseDto;
import org.englising.com.englisingbe.word.entity.Word;
import org.englising.com.englisingbe.word.entity.WordLike;
import org.englising.com.englisingbe.word.repository.WordLikeRepository;
import org.englising.com.englisingbe.word.repository.WordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class WordService {

    private final UserService userService;
    private final WordRepository wordRepository;
    private final WordLikeRepository wordLikeRepository;
    private final SinglePlayRepository singlePlayRepository;
    private final SinglePlayWordRepository singlePlayWordRepository;

    public Word getWordById(long wordId) {
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new GlobalException(ErrorHttpStatus.NO_MATCHING_WORD));
        return word;
    }

    public WordListResponseDto getWordList(WordListType type, Integer page, Integer size, Long userId) {
        switch (type) {
            case like -> {
                return getLikedWords(page, size, userId);
            }
            case played -> {
                return getPlayedWords(page, size, userId);
            }
            case searched -> {
                return getSearchedWords(page, size, userId);
            }
        }
        return null;
    }

    private WordListResponseDto getPlayedWords(Integer page, Integer size, Long userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"createdAt"));
        Page<SinglePlay> singlePlays = singlePlayRepository.getSinglePlayByUserUserId(userId, pageable);
        List<Long> singlePlayIds =  singlePlays.stream()
                .map(SinglePlay::getSinglePlayId)
                .toList();
        List<SinglePlayWord> singlePlayWords = singlePlayWordRepository
                .findBySinglePlaySinglePlayIdIn(singlePlayIds);

        // HashSet을 사용하여 중복을 제거한 후, 다시 ArrayList로 변환
        List<Word> uniqueWords = new ArrayList<>(new HashSet<>(singlePlayWords.stream()
                .map(SinglePlayWord::getWord) // SinglePlayWord에서 Word로 변환
                .toList()));
        List<WordResponseDto> wordList = uniqueWords.stream()
                .map(word -> {
                    return getWordResponseDto(word, userId);
                }).toList();

        return getWordListDtoByPageAndList(wordList, singlePlays);
    }

    private WordListResponseDto getLikedWords(Integer page, Integer size, Long userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt", "createdAt"));
        Page<WordLike> wordLikes = wordLikeRepository.getWordLikeByUserUserIdAndIsLikedTrue(userId, pageable);
        List<WordResponseDto> wordList = wordLikes
                .stream()
                .map(word -> {
                    return getWordResponseDto(word.getWord(), userId);
                })
                .toList();

        return getWordListDtoByPageAndList(wordList, wordLikes);
    }

    private WordListResponseDto getSearchedWords(Integer page, Integer size, Long userId) {
        // todo. 추후 가능하면 (검색한 단어)
        return null;
    }

    public WordLikeResponseDto likeWord(Long wordId, Long userId) {
        WordLike wordLike = wordLikeRepository.findByWordIdAndUserId(wordId, userId).orElse(null);

        if (wordLike == null) {
            User user = userService.getUserById(userId);
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

    public boolean isLikedByUserIdAndWordId(Long wordId, Long userId){
        return wordLikeRepository.existsByUserUserIdAndWordWordIdAndIsLikedTrue(userId, wordId);
    }

    private WordResponseDto getWordResponseDto(Word word, Long userId) {
        return WordResponseDto.builder()
                .wordId(word.getWordId())
                .enText(word.getEnText())
                .korText1(word.getKoText1())
                .korText2(word.getKoText2())
                .korText3(word.getKoText3())
                .example(word.getExample())
                .example(word.getExample())
                .liked(wordLikeRepository
                        .existsByUserUserIdAndWordWordIdAndIsLikedTrue(userId, word.getWordId()))
                .build();
    }
    private WordListResponseDto getWordListDtoByPageAndList(List<WordResponseDto> data, Page<?> page) {
        return WordListResponseDto.builder()
                .wordResponseDto(data)
                .pagination(PaginationDto.from(page))
                .build();
    }
}
