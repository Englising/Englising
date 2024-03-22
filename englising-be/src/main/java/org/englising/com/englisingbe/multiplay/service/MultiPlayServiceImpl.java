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
import org.englising.com.englisingbe.multiplay.entity.MultiPlay;
import org.englising.com.englisingbe.multiplay.repository.MultiPlayImgRepository;
import org.englising.com.englisingbe.multiplay.repository.MultiPlayRepository;
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
    private final TrackServiceImpl trackService;
    private final MultiPlaySetterService multiPlaySetterService;
    private final RedisServiceImpl redisService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    public Long createMultiPlay(MultiPlayRequestDto requestDto, Long userId) {
        MultiPlay multiPlay = multiPlayRepository
                .save(MultiPlay.getMultiPlayFromMultiPlayRequestDto(requestDto, trackService.getTrackByTrackId(158L)));
        MultiPlayGame game = getMultiPlayGameFromMultiPlay(multiPlay, userId);
        // Redis에 Game 저장
        redisService.saveMultiPlayGame(game);
        // Redis에 AnswerMap 저장
        redisService.saveAnswerMap(multiPlay.getMultiplayId(), multiPlaySetterService.getAnswerInputMapFromMultiPlaySentenceList(game.getSentences(), false));
        return multiPlay.getMultiplayId();
    }

    public List<MultiPlayListResponseDto> getMultiPlayWaitingList(Genre genre, Integer page, Integer size){
        List<MultiPlayGame> waitingGameList = redisService.getWaitingMultiPlayGames(genre, page, size);
        return waitingGameList.stream()
                .map(multiPlayGame -> {
                    return MultiPlayListResponseDto.builder()
                            .multiPlayId(multiPlayGame.getMultiPlayId())
                            .roomName(multiPlayGame.getRoomName())
                            .currentUser(multiPlayGame.getUsers().size())
                            .maxUser(multiPlayGame.getMaxUser())
                            .multiPlayImgUrl(multiPlayGame.getMultiplayImgUrl())
                            .genre(multiPlayGame.getGenre())
                            .build();
                }).toList();
    }

    public MultiPlayDetailResponseDto getMultiPlayById(Long multiPlayId, Long userId) {
        MultiPlayUser user = MultiPlayUser.getMultiPlayUserFromUser(userService.getUserById(userId));
        redisService.addNewUserToMultiPlayGame(multiPlayId, user);
        messagingTemplate.convertAndSend(WebSocketUrls.participantUrl + multiPlayId.toString(), user);
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

    public void startGame(Long multiplayId, Long userId){
        MultiPlayGame multiPlayGame = redisService.getMultiPlayGameById(multiplayId);
        if(!multiPlayGame.getManagerUserId().equals(userId)){
            throw new GlobalException(ErrorHttpStatus.UNAUTHORIZED_TOKEN);
        }
        MultiPlayWorker worker = new MultiPlayWorker(multiplayId, messagingTemplate, redisService);
        worker.sendRoundStartAlert();
    }

    public Boolean getMultiPlayResult(Long multiplayId) {
        return multiPlayRepository.findByMultiplayId(multiplayId).getIsSecret();
    }

    public String createRandomImg(){
        return multiPlayImgRepository.getRandomImageUrl();
    }

    private MultiPlayGame getMultiPlayGameFromMultiPlay(MultiPlay multiPlay, Long userId){
        List<MultiPlaySentence> sentences = multiPlaySetterService.getMultiPlaySentenceListFromTrack(multiPlay.getTrack().getTrackId());
        return MultiPlayGame.builder()
                .multiPlayId(multiPlay.getMultiplayId())
                .trackId(multiPlay.getTrack().getTrackId())
                .maxUser(multiPlay.getMaxUser())
                .roomName(multiPlay.getRoomName())
                .genre(multiPlay.getGenre())
                .isSecret(multiPlay.getIsSecret())
                .roomPw(multiPlay.getRoomPw())
                .multiplayImgUrl(multiPlay.getMultiPlayImgUrl())
                .sentences(sentences)
                .answerAlphabets(multiPlaySetterService.getAnswerInputMapFromMultiPlaySentenceList(sentences, true))
                .selectedHint(0) //TODO 힌트 랜덤 선택
                .managerUserId(userId)
                .users(new ArrayList<>())
                .round(1)
                .status(MultiPlayStatus.WAITING)
                .build();
    }
}