package org.englising.com.englisingbe.singleplay.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.englising.com.englisingbe.singleplay.dto.RightWordCntDto;
import org.englising.com.englisingbe.singleplay.dto.request.WordCheckRequestDto;
import org.englising.com.englisingbe.singleplay.dto.response.LyricDto;
import org.englising.com.englisingbe.singleplay.entity.SinglePlay;
import org.englising.com.englisingbe.singleplay.entity.SinglePlayWord;
import org.englising.com.englisingbe.singleplay.repository.SinglePlayWordRepository;
import org.englising.com.englisingbe.word.entity.TrackWord;
import org.englising.com.englisingbe.word.entity.Word;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SinglePlayWordService {
    private final SinglePlayWordRepository singlePlayWordRepository;

    public List<SinglePlayWord> createSinglePlayWords(List<TrackWord> selectedWords, List<LyricDto> lyrics, SinglePlay singlePlay){
        List<SinglePlayWord> singlePlayWordList = new ArrayList<>(trackWordsToSinglePlayWords(selectedWords, lyrics, singlePlay));
        Collections.sort(singlePlayWordList, new Comparator<SinglePlayWord>() {
            @Override
            public int compare(SinglePlayWord o1, SinglePlayWord o2) {
                if(o1.getSentenceIndex() == o2.getSentenceIndex()){
                    return o1.getWordIndex() - o2.getWordIndex();
                }
                return o1.getSentenceIndex() - o2.getSentenceIndex();
            }
        });
        return saveSinglePlayWords(singlePlayWordList);
    }

    public SinglePlayWord checkWordAnswer(WordCheckRequestDto wordCheckRequestDto){
        SinglePlayWord singlePlayWord = getSinglePlayWordById(wordCheckRequestDto.singleplayWordId);
        if (singlePlayWord.getOriginWord().equals(wordCheckRequestDto.getWord())){
            singlePlayWord.setIsRight(true);
        }
        else {
            singlePlayWord.setIsRight(false);
        }
        return singlePlayWordRepository.save(singlePlayWord);
    }

    public RightWordCntDto getRightAndTotalCnt(Long singlePlayId){
        List<SinglePlayWord> wordList = singlePlayWordRepository.getAllBySinglePlaySinglePlayId(singlePlayId)
                .orElseThrow(()-> new GlobalException(ErrorHttpStatus.NO_MACHING_SINGLEPLAY));
        return RightWordCntDto.builder()
                .totalWordCnt(wordList.size())
                .rightWordCnt((int) wordList.stream()
                        .filter(SinglePlayWord::getIsRight)
                        .count())
                .build();
    }

    public List<SinglePlayWord> getAllSinglePlayWordsBySinglePlayId(Long singlePlayId){
        return singlePlayWordRepository.getAllBySinglePlaySinglePlayId(singlePlayId)
                .orElseThrow(()-> new GlobalException(ErrorHttpStatus.NO_MACHING_SINGLEPLAY));
    }

    public SinglePlayWord getSinglePlayWordById(Long id){
        return singlePlayWordRepository.findById(id)
                .orElseThrow(()-> new GlobalException(ErrorHttpStatus.NO_MATCHING_SINGLEPLAYWORD));
    }

    private List<SinglePlayWord> trackWordsToSinglePlayWords(List<TrackWord> selectedWords, List<LyricDto> lyrics, SinglePlay singlePlay){
        return selectedWords.stream()
                .map(trackWord -> {
                    return SinglePlayWord.builder()
                            .singlePlay(singlePlay)
                            .word(trackWord.getWord())
                            .lyric(trackWord.getLyric())
                            .sentenceIndex((int) (trackWord.getLyric().getLyricId() - getFirstLyricId(lyrics)))
                            .wordIndex(trackWord.getWordIndex())
                            .originWord(trackWord.getOriginWord())
                            .isRight(false)
                            .build();
                }).toList();
    }

    private List<SinglePlayWord> saveSinglePlayWords(List<SinglePlayWord> words){
        return singlePlayWordRepository.saveAll(words);
    }

    private long getFirstLyricId(List<LyricDto> lyrics){
        return lyrics.stream()
                .min(Comparator.comparing(LyricDto::getLyricId))
                .get().getLyricId();
    }

}
