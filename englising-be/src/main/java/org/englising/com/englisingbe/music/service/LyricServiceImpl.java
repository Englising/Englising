package org.englising.com.englisingbe.music.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.englising.com.englisingbe.music.entity.Lyric;
import org.englising.com.englisingbe.music.repository.LyricRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LyricServiceImpl {
    private final LyricRepository lyricRepository;

    public List<Lyric> getAllLyricsByTrackId(Long trackId){
        return lyricRepository.findAllByTrackTrackId(trackId)
                .orElseThrow(() -> new GlobalException(ErrorHttpStatus.NO_MATCHING_TRACK));
    }
}
