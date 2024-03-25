package org.englising.com.englisingbe.singleplay.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.music.entity.Lyric;
import org.englising.com.englisingbe.singleplay.entity.SinglePlay;
import org.englising.com.englisingbe.singleplay.entity.SinglePlayWord;
import org.englising.com.englisingbe.singleplay.repository.SinglePlayWordRepository;
import org.englising.com.englisingbe.word.entity.TrackWord;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SinglePlayWordService {
    private final SinglePlayWordRepository singlePlayWordRepository;

    public List<SinglePlayWord> createSinglePlayWords(List<TrackWord> selectedWords, List<Lyric> lyrics, SinglePlay singlePlay){
        List<SinglePlayWord> singlePlayWordList = new ArrayList<>(trackWordsToSinglePlayWords(selectedWords, lyrics, singlePlay)); // 변경 가능한 리스트로 변환
        Collections.sort(singlePlayWordList, new Comparator<SinglePlayWord>() {
            @Override
            public int compare(SinglePlayWord o1, SinglePlayWord o2) {
                return o1.getSentenceIndex() - o2.getSentenceIndex();
            }
        });
        return saveSinglePlayWords(singlePlayWordList);
    }

    private List<SinglePlayWord> trackWordsToSinglePlayWords(List<TrackWord> selectedWords, List<Lyric> lyrics, SinglePlay singlePlay){
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

    private long getFirstLyricId(List<Lyric> lyrics){
        return lyrics.stream()
                .min(Comparator.comparing(Lyric::getLyricId))
                .get().getLyricId();
    }
}
