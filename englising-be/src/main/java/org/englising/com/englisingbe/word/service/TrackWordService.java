package org.englising.com.englisingbe.word.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.word.entity.TrackWord;
import org.englising.com.englisingbe.word.repository.TrackWordRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackWordService {
    private final TrackWordRepository trackWordRepository;

    public List<TrackWord> getRandomTrackWords(Long trackId){
        List<TrackWord> trackWordList = trackWordRepository.findTrackWordByTrackTrackId(trackId);
        Collections.shuffle(trackWordList);
        return trackWordList.subList(0, Math.min(trackWordList.size(), 15));
    }

}
