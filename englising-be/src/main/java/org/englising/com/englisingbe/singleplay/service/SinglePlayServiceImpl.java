package org.englising.com.englisingbe.singleplay.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.global.dto.PaginationDto;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.englising.com.englisingbe.global.util.PlayListType;
import org.englising.com.englisingbe.music.entity.Lyric;
import org.englising.com.englisingbe.music.service.LyricServiceImpl;
import org.englising.com.englisingbe.singleplay.dto.RightWordCntDto;
import org.englising.com.englisingbe.singleplay.dto.request.WordCheckRequestDto;
import org.englising.com.englisingbe.singleplay.dto.response.*;
import org.englising.com.englisingbe.singleplay.entity.SinglePlay;
import org.englising.com.englisingbe.singleplay.entity.SinglePlayHint;
import org.englising.com.englisingbe.singleplay.entity.SinglePlayWord;
import org.englising.com.englisingbe.singleplay.repository.SinglePlayHintRepository;
import org.englising.com.englisingbe.singleplay.repository.SinglePlayRepository;
import org.englising.com.englisingbe.music.dto.TrackAlbumArtistDto;
import org.englising.com.englisingbe.like.entity.TrackLike;
import org.englising.com.englisingbe.like.service.TrackLikeServiceImpl;
import org.englising.com.englisingbe.music.service.TrackServiceImpl;
import org.englising.com.englisingbe.user.service.UserService;
import org.englising.com.englisingbe.word.entity.TrackWord;
import org.englising.com.englisingbe.word.service.TrackWordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SinglePlayServiceImpl {
    private final TrackLikeServiceImpl trackLikeService;
    private final TrackWordService trackWordService;
    private final TrackServiceImpl trackService;
    private final UserService userService;
    private final LyricServiceImpl lyricService;

    private final SinglePlayRepository singlePlayRepository;
    private final SinglePlayHintRepository singlePlayHintRepository;
    private final SinglePlayWordService singlePlayWordService;

    public PlayListDto getPlayList(PlayListType type, Integer page, Integer size, Long userId) {
        switch (type) {
            case like -> {
                return getLikedTracks(page, size, userId);
            }
            case recent -> {
                return getRecentTracks(page, size, userId);
            }
            case recommend -> {
                return getRecommendedTracks(page, size, userId);
            }
        }
        return null;
    }

    public SinglePlayResponseDto createSinglePlay(Long userId, Long trackId, Integer singlePlayLevelId){
        // TODO
        // SinglePlay Repository에 Create
        SinglePlay singlePlay = singlePlayRepository.save(SinglePlay.builder()
                        .singlePlayHint(getSinglePlayHintById(singlePlayLevelId))
                        .user(userService.getUserById(userId))
                        .track(trackService.getTrackByTrackId(trackId))
                .build());
        // 해당 Track의 Lyrics 가져옴
        //TODO : trackId 변경
        List<Lyric> lyrics = lyricService.getAllLyricsByTrackId(trackId);
        // 해당 Track의 Random Words 가져옴
        //TODO : Random 아니게 변경
        List<TrackWord> selectedWords = trackWordService.getRandomTrackWords(trackId);
        // SinglePlay-Word에 저장
        List<SinglePlayWord> singlePlayWordList = singlePlayWordService.createSinglePlayWords(selectedWords, lyrics, singlePlay);
        // Dto 생성
        return SinglePlayResponseDto.builder()
                .singlePlayId(singlePlay.getSinglePlayId())
                .lyrics(getLyricDtoFromLyricList(lyrics, singlePlayWordList))
                .words(getWordDtoFromSinglePlayWord(singlePlayWordList))
                .totalWordCnt(singlePlayWordList.size())
                .rightWordCnt((int) singlePlayWordList.stream()
                        .filter(SinglePlayWord::getIsRight)
                        .count())
                .build();
    }

    public WordCheckResponseDto checkWord(WordCheckRequestDto wordCheckRequestDto){
        SinglePlayWord singlePlayWord = singlePlayWordService.checkWordAnswer(wordCheckRequestDto);
        RightWordCntDto rightWordCntDto = singlePlayWordService.getRightAndTotalCnt(wordCheckRequestDto.singleplayId);
        return WordCheckResponseDto.builder()
                .word(WordResponseDto.builder()
                        .singleplayWordId(singlePlayWord.getSinglePlayWordId())
                        .sentenceIndex(singlePlayWord.getSentenceIndex())
                        .wordIndex(singlePlayWord.getWordIndex())
                        .word(singlePlayWord.getOriginWord())
                        .isRight(singlePlayWord.getIsRight())
                        .build())
                .totalWordCnt(rightWordCntDto.getTotalWordCnt())
                .rightWordCnt(rightWordCntDto.getRightWordCnt())
                .build();
    }

    public SinglePlayResponseDto getSinglePlayResult(Long singlePlayId){
        // SinglePlay Repository에서 조회
        SinglePlay singlePlay = getSinglePlayById(singlePlayId);
        // 해당 Track의 Lyrics 가져옴
        List<Lyric> lyrics = lyricService.getAllLyricsByTrackId(singlePlay.getTrack().getTrackId());
        // SinglePlay-Word에 저장
        List<SinglePlayWord> singlePlayWordList = singlePlayWordService.getAllSinglePlayWordsBySinglePlayId(singlePlayId);
        // Dto 생성
        return SinglePlayResponseDto.builder()
                .singlePlayId(singlePlay.getSinglePlayId())
                .lyrics(getLyricDtoFromLyricList(lyrics, singlePlayWordList))
                .words(getWordDtoFromSinglePlayWord(singlePlayWordList))
                .totalWordCnt(singlePlayWordList.size())
                .rightWordCnt((int) singlePlayWordList.stream()
                        .filter(SinglePlayWord::getIsRight)
                        .count())
                .build();
    }

    public TimeResponseDto getLyricStartTimes(Long trackId){
        List<Lyric> lyrics = lyricService.getAllLyricsByTrackId(trackId);
        return TimeResponseDto.builder()
                .trackId(trackId)
                .startTime(lyrics.stream()
                        .map(lyric -> lyric.getStartTime().floatValue()).toList())
                .build();
    }

    private SinglePlayHint getSinglePlayHintById(Integer singlePlayHintId){
        return singlePlayHintRepository.findSinglePlayHintBySingleplayLevelId(singlePlayHintId)
                .orElseThrow(()-> new GlobalException(ErrorHttpStatus.NO_MATCHING_HINT));
    }

    private PlayListDto getLikedTracks(Integer page, Integer size, Long userId){
        Page<TrackLike> trackLikes = trackLikeService.getLikedTrackResponseDtoByUserId(userId, page, size);
        List<TrackAlbumArtistDto> tracks = trackService.getTrackAlbumArtistsByTrackIds(trackLikes.getContent()
                .stream()
                .map( trackLike -> {
                            return trackLike.getTrack().getTrackId();
                        }
                ).collect(Collectors.toList()));
        return getPlayListDtoFromPageAndList(getTrackResponseDtoFromTrackAlbumArtist(userId, tracks),trackLikes);
    }

    private PlayListDto getRecentTracks(Integer page, Integer size, Long userId){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<SinglePlay> singlePlays = singlePlayRepository.getSinglePlayByUserUserId(userId, pageable);
        List<TrackAlbumArtistDto> tracks = trackService.getTrackAlbumArtistsByTrackIds(singlePlays
                .stream()
                .map(singlePlay -> {
                    return singlePlay.getTrack().getTrackId();
        }).collect(Collectors.toList()));
        return getPlayListDtoFromPageAndList(getTrackResponseDtoFromTrackAlbumArtist(userId,tracks), singlePlays);
    }

    private PlayListDto getRecommendedTracks(Integer page, Integer size, Long userId){
        // TODO FastAPI를 통해 추천 플레이리스트 가져오는 로직 구현
        return getLikedTracks(page, size, userId);
    }

    private List<TrackResponseDto> getTrackResponseDtoFromTrackAlbumArtist(Long userId, List<TrackAlbumArtistDto> trackAlbumArtistDtos){
        return trackAlbumArtistDtos
                .stream()
                .map(trackAlbumArtistDto -> {
                    return TrackResponseDto.getTrackResponseFromTrackAlbumArtist(
                            trackAlbumArtistDto,
                            0,
                            trackLikeService.checkTrackLikeByUserId(userId, trackAlbumArtistDto.getTrack().getTrackId())
                    );
                }).toList();
    }

    private PlayListDto getPlayListDtoFromPageAndList(List<TrackResponseDto> data, Page<?> page){
        return PlayListDto.builder()
                .playList(data)
                .pagination(PaginationDto.from(page))
                .build();
    }

    private SinglePlay getSinglePlayById(Long singlePlayId){
        return singlePlayRepository.findById(singlePlayId)
                .orElseThrow(()-> new GlobalException(ErrorHttpStatus.NO_MACHING_SINGLEPLAY));
    }

    private List<LyricDto> getLyricDtoFromLyricList(List<Lyric> lyricList, List<SinglePlayWord> singlePlayWordList) {
        return lyricList.stream()
                .map(lyric -> {
                    boolean isBlank = singlePlayWordList.stream()
                            .anyMatch(spw -> spw.getLyric().getLyricId().equals(lyric.getLyricId()));
                    return LyricDto.builder()
                            .isBlank(isBlank)
                            .startTime(lyric.getStartTime().floatValue())
                            .endTime(lyric.getEndTime().floatValue())
                            .lyric(Arrays.asList(lyric.getEnText().split(" ")))
                            .build();
                }).collect(Collectors.toList());
    }

    private List<WordResponseDto> getWordDtoFromSinglePlayWord(List<SinglePlayWord> singlePlayWordList){
        return singlePlayWordList.stream()
                .map(singlePlayWord -> {
                    return WordResponseDto.builder()
                            .singleplayWordId(singlePlayWord.getSinglePlayWordId())
                            .sentenceIndex(singlePlayWord.getSentenceIndex())
                            .wordIndex(singlePlayWord.getWordIndex())
                            .word(singlePlayWord.getOriginWord())
                            .isRight(singlePlayWord.getIsRight())
                            .build();
                }).toList();
    }
}
