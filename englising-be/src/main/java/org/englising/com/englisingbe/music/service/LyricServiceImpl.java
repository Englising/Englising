package org.englising.com.englisingbe.music.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.englising.com.englisingbe.music.entity.Lyric;
import org.englising.com.englisingbe.music.repository.LyricRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LyricServiceImpl {
    private final LyricRepository lyricRepository;

    public List<Lyric> getAllLyricsByTrackId(Long trackId){
        return lyricRepository.findAllByTrackTrackId(trackId)
                .orElseThrow(() -> new GlobalException(ErrorHttpStatus.NO_MATCHING_TRACK));
    }

    public Long getLyricIdByStartTimeAndTrackId(Long trackId, BigDecimal startTime){
        Optional<Lyric> lyric = lyricRepository.findByTrackTrackIdAndStartTime(trackId, startTime);
        return lyric.map(Lyric::getLyricId).orElse(null);
    }

    public Lyric getLyricByLyricId(Long lyricId){
        return lyricRepository.findById(lyricId)
                .orElseThrow(()-> new GlobalException(ErrorHttpStatus.NO_MATCHING_LYRIC));
    }
}
