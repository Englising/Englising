package org.englising.com.englisingbe.multiplay.service;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.englising.com.englisingbe.global.util.Genre;
import org.englising.com.englisingbe.global.util.MultiPlayStatus;
import org.englising.com.englisingbe.global.util.WebSocketUrls;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlayGame;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlaySentence;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlayUser;
import org.englising.com.englisingbe.multiplay.dto.request.MultiPlayRequestDto;
import org.englising.com.englisingbe.multiplay.dto.response.MultiPlayDetailResponseDto;
import org.englising.com.englisingbe.multiplay.dto.response.MultiPlayListResponseDto;
import org.englising.com.englisingbe.multiplay.dto.socket.ParticipantDto;
import org.englising.com.englisingbe.multiplay.entity.MultiPlay;
import org.englising.com.englisingbe.multiplay.repository.MultiPlayHintRepository;
import org.englising.com.englisingbe.multiplay.repository.MultiPlayImgRepository;
import org.englising.com.englisingbe.multiplay.repository.MultiPlayRepository;
import org.englising.com.englisingbe.music.entity.Lyric;
import org.englising.com.englisingbe.music.service.LyricServiceImpl;
import org.englising.com.englisingbe.music.service.TrackServiceImpl;
import org.englising.com.englisingbe.redis.service.RedisServiceImpl;
import org.englising.com.englisingbe.user.service.UserService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MultiPlayServiceImpl {
    private final MultiPlayRepository multiPlayRepository;
    private final MultiPlayImgRepository multiPlayImgRepository;
    private final MultiPlayHintRepository multiPlayHintRepository;

    private final TrackServiceImpl trackService;
    private final MultiPlaySetterService multiPlaySetterService;
    private final RedisServiceImpl redisService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;
    private final AnswerQueueService answerQueueService;
    private final LyricServiceImpl lyricService;

    public Long createMultiPlay(MultiPlayRequestDto requestDto, Long userId) {
        MultiPlay multiPlay = multiPlayRepository
                .save(MultiPlay.getMultiPlayFromMultiPlayRequestDto(requestDto, trackService.getTrackByTrackId(158L))); //TODO 노래 선정 수정
        MultiPlayGame game = getMultiPlayGameFromMultiPlay(multiPlay, userId);
        // Redis에 Game 저장
        redisService.saveMultiPlayGame(game);
        // Redis에 AnswerMap 저장
        redisService.saveAnswerMap(multiPlay.getMultiplayId(), multiPlaySetterService.getAnswerInputMapFromMultiPlaySentenceList(game.getSentences(), false));
        // AnswerQueue 생성, 시작
        answerQueueService.createQueue(multiPlay.getMultiplayId());
        answerQueueService.processQueue(multiPlay.getMultiplayId());
        return multiPlay.getMultiplayId();
    }

    public List<MultiPlayListResponseDto> getMultiPlayWaitingList(Genre genre){
        List<MultiPlayGame> waitingGameList = redisService.getWaitingMultiPlayGames(genre);
        return waitingGameList.stream()
                .map(multiPlayGame -> {
                    return MultiPlayListResponseDto.builder()
                            .multiPlayId(multiPlayGame.getMultiPlayId())
                            .roomName(multiPlayGame.getRoomName())
                            .currentUser(multiPlayGame.getUsers().size())
                            .maxUser(multiPlayGame.getMaxUser())
                            .multiPlayImgUrl(multiPlayGame.getMultiplayImgUrl())
                            .genre(multiPlayGame.getGenre())
                            .isSecret(multiPlayGame.isSecret())
                            .password(multiPlayGame.getRoomPw())
                            .build();
                }).toList();
    }

    public MultiPlayDetailResponseDto getMultiPlayById(Long multiPlayId) {
        // MultiPlay 게임 방 정보 반환
        MultiPlayGame multiPlayGame = redisService.getMultiPlayGameById(multiPlayId);
        return MultiPlayDetailResponseDto.builder()
                .multiPlayId(multiPlayGame.getMultiPlayId())
                .roomName(multiPlayGame.getRoomName())
                .multiPlayImgUrl(multiPlayGame.getMultiplayImgUrl())
                .genre(multiPlayGame.getGenre())
                .managerUserId(multiPlayGame.getManagerUserId())
                .currentUser(multiPlayGame.getUsers())
                .maxUser(multiPlayGame.getMaxUser())
                .isSecret(multiPlayGame.isSecret())
                .build();
    }

    public void enterMultiPlay(Long multiPlayId, Long userId) {
        MultiPlayUser user = MultiPlayUser.getMultiPlayUserFromUser(userService.getUserById(userId));
        // Redis에 게임 사용자 업데이트
        boolean result = redisService.addNewUserToMultiPlayGame(multiPlayId, user);
        // 다른 참여자들에게 입장 알림
        if(result){
            messagingTemplate.convertAndSend(WebSocketUrls.participantUrl + multiPlayId.toString(),
                    ParticipantDto.builder()
                            .kind("enter")
                            .user(user)
                            .build());
        }
        else {
            throw new GlobalException(ErrorHttpStatus.FULL_MULTIPLAY_ROOM);
        }
    }

    public void startGame(Long multiplayId, Long userId){
        MultiPlayGame multiPlayGame = redisService.getMultiPlayGameById(multiplayId);
        // 요청한 유저가 방장인지 확인
        if(!multiPlayGame.getManagerUserId().equals(userId)){
            throw new GlobalException(ErrorHttpStatus.UNAUTHORIZED_TOKEN);
        }
        MultiPlayWorker worker = new MultiPlayWorker(multiplayId, messagingTemplate, redisService);
        worker.sendRoundStartAlert();
    }

    public void leaveGame(Long multiPlayId, Long userId){
        MultiPlayUser user = MultiPlayUser.getMultiPlayUserFromUser(userService.getUserById(userId));
        boolean result = redisService.deleteUserToMultiPlayGame(multiPlayId, user);
        if(result){
            messagingTemplate.convertAndSend(WebSocketUrls.participantUrl + multiPlayId.toString(),
                    ParticipantDto.builder()
                            .kind("leave")
                            .user(user)
                            .build());
        }
    }

    public Boolean getMultiPlayResult(Long multiplayId) {
        return multiPlayRepository.findByMultiplayId(multiplayId).getIsSecret();
    }

    public String createRandomImg(){
        return multiPlayImgRepository.getRandomImageUrl();
    }

    private MultiPlayGame getMultiPlayGameFromMultiPlay(MultiPlay multiPlay, Long userId){
        List<MultiPlaySentence> sentences = multiPlaySetterService.getMultiPlaySentenceListFromTrack(multiPlay.getTrack().getTrackId());
        long startLyricId = lyricService.getLyricIdByStartTimeAndTrackId(multiPlay.getTrack().getTrackId(), sentences.get(0).getStartTime());
        long endLyricId = lyricService.getLyricIdByStartTimeAndTrackId(multiPlay.getTrack().getTrackId(), sentences.get(sentences.size()-1).getStartTime());
        Lyric beforeLyric = lyricService.getLyricByLyricId(startLyricId-1);
        Lyric afterLyric = lyricService.getLyricByLyricId(endLyricId+1);
        return MultiPlayGame.builder()
                .multiPlayId(multiPlay.getMultiplayId())
                .track(multiPlay.getTrack())
                .maxUser(multiPlay.getMaxUser())
                .roomName(multiPlay.getRoomName())
                .genre(multiPlay.getGenre())
                .isSecret(multiPlay.getIsSecret())
                .roomPw(multiPlay.getRoomPw())
                .multiplayImgUrl(multiPlay.getMultiPlayImgUrl())
                .beforeLyric(beforeLyric.getEnText())
                .beforeLyricStartTime(beforeLyric.getStartTime())
                .afterLyric(afterLyric.getEnText())
                .afterLyricEndTime(afterLyric.getEndTime())
                .sentences(sentences)
                .answerAlphabets(multiPlaySetterService.getAnswerInputMapFromMultiPlaySentenceList(sentences, true))
                .selectedHint(Math.toIntExact(multiPlayHintRepository.findRandom().getMultiplayHintId()))
                .managerUserId(userId)
                .users(new ArrayList<>())
                .round(1)
                .status(MultiPlayStatus.WAITING)
                .build();
    }
}