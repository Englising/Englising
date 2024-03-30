package org.englising.com.englisingbe.singleplay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.englising.com.englisingbe.global.dto.PaginationDto;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.englising.com.englisingbe.global.util.PlayListType;
import org.englising.com.englisingbe.multiplay.dto.request.TrackWordFastApiDto;
import org.englising.com.englisingbe.music.entity.Lyric;
import org.englising.com.englisingbe.music.entity.Track;
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
import org.englising.com.englisingbe.webclient.service.RecommendApiService;
import org.englising.com.englisingbe.word.entity.TrackWord;
import org.englising.com.englisingbe.word.repository.TrackWordRepository;
import org.englising.com.englisingbe.word.service.TrackWordService;
import org.englising.com.englisingbe.word.service.WordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SinglePlayServiceImpl {
    private final TrackLikeServiceImpl trackLikeService;
    private final TrackServiceImpl trackService;
    private final UserService userService;
    private final LyricServiceImpl lyricService;
    private final WordService wordService;

    private final SinglePlayRepository singlePlayRepository;
    private final SinglePlayHintRepository singlePlayHintRepository;
    private final TrackWordRepository trackWordRepository;
    private final SinglePlayWordService singlePlayWordService;
    private final RecommendApiService recommendApiService;

    public PlayListDto getPlayList(PlayListType type, Integer page, Integer size, Long userId) {
        switch (type) {
            case like -> {
                return getLikedTracks(page, size, userId);
            }
            case recent -> {
                return getRecentTracks(page, size, userId);
            }
        }
        return null;
    }

    public List<TrackResponseDto> getRecommendedTracks(Long userId){
        List<Long> tracks = recommendApiService.getRecommendTrackByUserId(userId);
        return getTrackResponseDtoFromTrackAlbumArtist(userId, trackService.getTrackAlbumArtistsByTrackIds(tracks));
    }

    public SinglePlayResponseDto createSinglePlay(Long userId, Long trackId, Integer singlePlayLevelId){
        // SinglePlay Repository에 Create
        SinglePlay singlePlay = singlePlayRepository.save(SinglePlay.builder()
                        .singlePlayHint(getSinglePlayHintById(singlePlayLevelId))
                        .user(userService.getUserById(userId))
                        .track(trackService.getTrackByTrackId(trackId))
                .build());
        // Lyric이 토큰 단위로 잘린 Dto 가져옴
        List<LyricDto> lyricWordSplitted = recommendApiService.getSplittedWordsOfLyricByTrackId(trackId);
        // 해당 Track의 Random Words 가져옴
        List<TrackWord> selectedWords = recommendApiService.getSinglePlayGameWords(trackId, userId, singlePlayLevelId).stream()
                .map(this::trackWordFastApiDtoToTrackWord).toList();
        // SinglePlay-Word에 저장
        List<SinglePlayWord> singlePlayWordList = singlePlayWordService.createSinglePlayWords(selectedWords, lyricWordSplitted, singlePlay);
        // Dto 생성
        return SinglePlayResponseDto.builder()
                .singlePlayId(singlePlay.getSinglePlayId())
                .lyrics(getLyricDtoFromLyricList(lyricWordSplitted, singlePlayWordList))
                .words(getWordDtoFromSinglePlayWord(singlePlayWordList))
                .totalWordCnt(singlePlayWordList.size())
                .rightWordCnt((int) singlePlayWordList.stream()
                        .filter(SinglePlayWord::getIsRight)
                        .count())
                .build();
    }

    public WordCheckResponseDto checkWord(WordCheckRequestDto wordCheckRequestDto, Long userId){
        SinglePlayWord originWord = singlePlayWordService.getSinglePlayWordById(wordCheckRequestDto.getSingleplayWordId());
        if(!originWord.getSinglePlay().getUser().getUserId().equals(userId)){
            throw new GlobalException(ErrorHttpStatus.UNAUTHORIZED_TOKEN);
        }
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

    public SinglePlayResultResponseDto getSinglePlayResult(Long singlePlayId, Long userId){
        // SinglePlay Repository에서 조회
        SinglePlay singlePlay = getSinglePlayById(singlePlayId);
        if(!singlePlay.getUser().getUserId().equals(userId)){
            throw new GlobalException(ErrorHttpStatus.UNAUTHORIZED_TOKEN);
        }
        // 해당 Track의 Lyrics 가져옴
        List<LyricDto> lyricWordSplitted = recommendApiService.getSplittedWordsOfLyricByTrackId(singlePlay.getTrack().getTrackId());
        // SinglePlay-Word 결과 조회
        List<SinglePlayWord> singlePlayWordList = singlePlayWordService.getAllSinglePlayWordsBySinglePlayId(singlePlayId);
        int totalWordCnt = singlePlayWordList.size();
        int rightWordCnt = (int) singlePlayWordList.stream().filter(SinglePlayWord::getIsRight).count();
        int correctRate = rightWordCnt == 0? 0 : rightWordCnt / totalWordCnt * 100;
        singlePlayRepository.updateScoreAndCorrectRateById(singlePlayId, rightWordCnt, correctRate);
        // Dto 생성
        return SinglePlayResultResponseDto.builder()
                .singlePlayId(singlePlay.getSinglePlayId())
                .lyrics(getLyricDtoFromLyricList(lyricWordSplitted, singlePlayWordList))
                .words(getWordResultDtoFromSinglePlayWord(singlePlayWordList, userId))
                .totalWordCnt(totalWordCnt)
                .rightWordCnt(rightWordCnt)
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

    public PlayListDto getSearchTracks(String keyword, Integer page, Integer size, Long userId){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "spotifyPopularity"));
        Page<Track> trackIds = trackService.getSearchTrackIds(keyword, pageable);
        List<TrackAlbumArtistDto> tracks = trackService.getTrackAlbumArtistsByTrackIds(trackIds.getContent()
                .stream()
                .map(Track::getTrackId)
                .toList());
        return getPlayListDtoFromPageAndList(getTrackResponseDtoFromTrackAlbumArtist(userId, tracks),trackIds);
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
        }).toList());
        return getPlayListDtoFromPageAndList(getTrackResponseDtoFromTrackAlbumArtist(userId,tracks), singlePlays);
    }

    private List<TrackResponseDto> getTrackResponseDtoFromTrackAlbumArtist(Long userId, List<TrackAlbumArtistDto> trackAlbumArtistDtos){
        return trackAlbumArtistDtos
                .stream()
                .map(trackAlbumArtistDto ->
                        TrackResponseDto.getTrackResponseFromTrackAlbumArtist(
                            trackAlbumArtistDto,
                            getTrackSinglePlayScore(userId, trackAlbumArtistDto.getTrack().getTrackId()),
                            trackLikeService.checkTrackLikeByUserId(userId, trackAlbumArtistDto.getTrack().getTrackId()))
                ).toList();
    }

    private int getTrackSinglePlayScore(Long userId, Long trackId){
        int score = 0;
        List<Integer> scores = singlePlayRepository.findMaxScoresByUserIdAndTrackIdGroupedByLevel(userId, trackId);
        if(scores != null){
            int totalScore = 0;
            for (int i = 0; i < scores.size(); i++) {
                if(scores.get(i) != null){
                    totalScore += scores.get(i);
                }
            }
            if(totalScore == 0){
                return 0;
            }
            int total = 30+40+50;
            int rate = totalScore / total * 100;
            if (rate < 30) {
                score = 0;
            } else if (rate < 60) {
                score = 1;
            } else {
                score = 2;
            }
        }
        return score;
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

    private List<LyricDto> getLyricDtoFromLyricList(List<LyricDto> lyricList, List<SinglePlayWord> singlePlayWordList) {
        return lyricList.stream()
                .map(lyric -> {
                    lyric.setIsBlank(singlePlayWordList.stream()
                            .anyMatch(spw -> spw.getLyric().getLyricId().equals(lyric.getLyricId())));
                    return lyric;
                }).toList();
    }

    private List<WordResponseDto> getWordDtoFromSinglePlayWord(List<SinglePlayWord> singlePlayWordList){
        return singlePlayWordList.stream()
                .map(singlePlayWord ->
                        WordResponseDto.builder()
                            .singleplayWordId(singlePlayWord.getSinglePlayWordId())
                            .sentenceIndex(singlePlayWord.getSentenceIndex())
                            .wordIndex(singlePlayWord.getWordIndex())
                            .word(singlePlayWord.getOriginWord())
                            .isRight(singlePlayWord.getIsRight())
                            .build()
                ).toList();
    }
    private List<WordResultResponseDto> getWordResultDtoFromSinglePlayWord(List<SinglePlayWord> singlePlayWordList, Long userId){
        return singlePlayWordList.stream()
                .map(singlePlayWord ->
                        WordResultResponseDto.builder()
                                .singleplayWordId(singlePlayWord.getSinglePlayWordId())
                                .sentenceIndex(singlePlayWord.getSentenceIndex())
                                .wordIndex(singlePlayWord.getWordIndex())
                                .word(singlePlayWord.getOriginWord())
                                .isRight(singlePlayWord.getIsRight())
                                .wordId(singlePlayWord.getWord().getWordId())
                                .isLike(wordService.isLikedByUserIdAndWordId(singlePlayWord.getWord().getWordId(), userId))
                                .build()
                ).toList();
    }

    private TrackWord trackWordFastApiDtoToTrackWord(TrackWordFastApiDto trackWordFastApiDto){
        return trackWordRepository.findById(trackWordFastApiDto.getTrack_word_id()).get();
    }
}
