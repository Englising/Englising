package org.englising.com.englisingbe.multiplay.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlayAlphabet;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlaySentence;
import org.englising.com.englisingbe.music.entity.Lyric;
import org.englising.com.englisingbe.music.service.LyricServiceImpl;
import org.springframework.stereotype.Service;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlayWord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.englising.com.englisingbe.global.util.StringUtils.symbols;

@Service
@RequiredArgsConstructor
public class MultiPlaySetterService {
    private final LyricServiceImpl lyricService;
    private final int symbolIndex = -1;
    private final int lyricTotal = 3;

    public List<MultiPlaySentence> getMultiPlaySentenceListFromTrack(Long trackId){
        List<Lyric> lyrics = lyricService.getAllLyricsByTrackId(trackId);
        int startIndex = getRandomIndex(lyrics);
        List<Lyric> selectedLyrics = lyrics.subList(startIndex, startIndex+lyricTotal);
        return getMultiPlaySentenceFromLyric(selectedLyrics);
    }

    private int getRandomIndex(List<Lyric> lyrics){
        Random random = new Random();
        return random.nextInt(lyrics.size() - lyricTotal +1);
    }

    private List<MultiPlaySentence> getMultiPlaySentenceFromLyric(List<Lyric> lyrics){
        return IntStream.range(0,lyrics.size())
                .mapToObj(index -> MultiPlaySentence.builder()
                        .sentenceIndex(index)
                        .startTime(lyrics.get(index).getStartTime())
                        .endTime(lyrics.get(index).getEndTime())
                        .alphabets(getMultiPlayWordFromSentence(lyrics.get(index).getEnText())).build()
                )
                .toList();
    }

    private List<MultiPlayWord> getMultiPlayWordFromSentence(String sentence){
        List<String> words = Arrays.asList(sentence.split(" "));
        return IntStream.range(0, words.size())
                .mapToObj(index -> MultiPlayWord.builder()
                        .wordIndex(index)
                        .word(getMultiPlayAlphabetListFromString(words.get(index)))
                        .build())
                .toList();
    }

    private List<MultiPlayAlphabet> getMultiPlayAlphabetListFromString(String word){
        List<String> alphabets = Arrays.asList(word.split(""));
        List<MultiPlayAlphabet> result = new ArrayList<>();
        int index = 1;
        for(String alphabet: alphabets){
            if(symbols.contains(alphabet)){
                result.add(MultiPlayAlphabet.builder()
                        .index(symbolIndex)
                        .word(alphabet)
                        .build());
            }
            else {
                result.add(MultiPlayAlphabet.builder()
                        .index(index)
                        .word(alphabet)
                        .build());
                index++;
            }
        }
        return result;
    }
}
