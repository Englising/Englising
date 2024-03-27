package org.englising.com.englisingbe.multiplay.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlayAlphabet;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlaySentence;
import org.englising.com.englisingbe.music.entity.Lyric;
import org.englising.com.englisingbe.music.service.LyricServiceImpl;
import org.springframework.stereotype.Service;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlayWord;

import java.util.*;
import java.util.stream.IntStream;

import static org.englising.com.englisingbe.global.util.StringUtils.symbols;

@Service
@RequiredArgsConstructor
public class MultiPlaySetterService {
    private final LyricServiceImpl lyricService;
    private final int symbolIndex = -1;
    private final int lyricTotal = 3;
    private int index = 1;

    public Map<Integer, String> getAnswerInputMapFromMultiPlaySentenceList(List<MultiPlaySentence> sentences, boolean answer){
        Map<Integer, String> answerMap = new HashMap<>();
        for (MultiPlaySentence sentence: sentences){
            for(MultiPlayWord word: sentence.getWords()){
                for(MultiPlayAlphabet alphabet: word.getAlphabets()){
                    if(alphabet.getAlphabetIndex() != -1){
                        if(answer){
                            answerMap.put(alphabet.getAlphabetIndex(), alphabet.getAlphabet());
                        }
                        else {
                            answerMap.put(alphabet.getAlphabetIndex(), "-");
                        }
                    }
                }
            }
        }
        return answerMap;
    }

    public List<MultiPlaySentence> getMultiPlaySentenceListFromTrack(Long trackId){
        List<Lyric> lyrics = lyricService.getAllLyricsByTrackId(trackId);
        int startIndex = getRandomIndex(lyrics);
        List<Lyric> selectedLyrics = lyrics.subList(startIndex, startIndex+lyricTotal);
        index = 1;
        return getMultiPlaySentenceFromLyric(selectedLyrics);
    }

    private int getRandomIndex(List<Lyric> lyrics){
        Random random = new Random();
        return 1 + random.nextInt(lyrics.size() - lyricTotal + 2);
    }

    private List<MultiPlaySentence> getMultiPlaySentenceFromLyric(List<Lyric> lyrics){
        return IntStream.range(0,lyrics.size())
                .mapToObj(index -> MultiPlaySentence.builder()
                        .sentenceIndex(index)
                        .startTime(lyrics.get(index).getStartTime())
                        .endTime(lyrics.get(index).getEndTime())
                        .words(getMultiPlayWordFromSentence(lyrics.get(index).getEnText())).build()
                )
                .toList();
    }

    private List<MultiPlayWord> getMultiPlayWordFromSentence(String sentence){
        List<String> words = Arrays.asList(sentence.split(" "));
        return IntStream.range(0, words.size())
                .mapToObj(index -> MultiPlayWord.builder()
                        .wordIndex(index)
                        .alphabets(getMultiPlayAlphabetListFromString(words.get(index)))
                        .build())
                .toList();
    }

    private List<MultiPlayAlphabet> getMultiPlayAlphabetListFromString(String word){
        List<String> alphabets = Arrays.asList(word.split(""));
        List<MultiPlayAlphabet> result = new ArrayList<>();
        for(String alphabet: alphabets){
            if(symbols.contains(alphabet)){
                result.add(MultiPlayAlphabet.builder()
                        .alphabetIndex(symbolIndex)
                        .alphabet(alphabet)
                        .build());
            }
            else {
                result.add(MultiPlayAlphabet.builder()
                        .alphabetIndex(index)
                        .alphabet(alphabet.toLowerCase())
                        .build());
                index++;
            }
        }
        return result;
    }
}
