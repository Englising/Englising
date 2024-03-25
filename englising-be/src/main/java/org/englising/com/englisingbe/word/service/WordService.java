package org.englising.com.englisingbe.word.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.englising.com.englisingbe.global.dto.PaginationDto;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.englising.com.englisingbe.music.dto.TrackAlbumArtistDto;
import org.englising.com.englisingbe.singleplay.dto.response.PlayListDto;
import org.englising.com.englisingbe.singleplay.dto.response.TrackResponseDto;
import org.englising.com.englisingbe.user.entity.User;
import org.englising.com.englisingbe.user.repository.UserRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        return null;
    }

    private WordListResponseDto getLikedWords(Integer page, Integer size, Long userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt", "createdAt"));
        Page<WordLike> wordLikes = wordLikeRepository.getWordLikeByUserUserId(userId, pageable);
//        Page<Word> words = wordRepository.findLikeWordsByUserId(userId, pageable);
        List<WordResponseDto> wordList = wordLikes
                .stream()
                .map(wordLike -> {
                    return WordResponseDto.builder()
                            .wordId(wordLike.getWord().getWordId())
                            .enText(wordLike.getWord().getEnText())
                            .korText1(wordLike.getWord().getKoText1())
                            .korText2(wordLike.getWord().getKoText2())
                            .korText3(wordLike.getWord().getKoText3())
                            .example(wordLike.getWord().getExample())
                            .isLiked(wordLike.getIsLiked())
                            .build();
                })
                .toList();

        return getWordListDtoByPageAndList(wordList, wordLikes);
    }


    private WordListResponseDto getWordListDtoByPageAndList(List<WordResponseDto> data, Page<?> page) {
        return WordListResponseDto.builder()
                .wordResponseDto(data)
                .pagination(PaginationDto.from(page))
                .build();
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
}
