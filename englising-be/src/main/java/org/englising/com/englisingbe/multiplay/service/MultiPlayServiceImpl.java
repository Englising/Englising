package org.englising.com.englisingbe.multiplay.service;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.englising.com.englisingbe.global.util.Genre;
import org.englising.com.englisingbe.global.util.MultiPlayStatus;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlayGame;
import org.englising.com.englisingbe.multiplay.dto.request.MultiPlayRequestDto;
import org.englising.com.englisingbe.multiplay.dto.response.MultiPlayDetailResponseDto;
import org.englising.com.englisingbe.multiplay.dto.response.MultiPlayListResponseDto;
import org.englising.com.englisingbe.multiplay.entity.MultiPlay;
import org.englising.com.englisingbe.multiplay.repository.MultiPlayImgRepository;
import org.englising.com.englisingbe.multiplay.repository.MultiPlayRepository;
import org.englising.com.englisingbe.music.service.TrackServiceImpl;
import org.englising.com.englisingbe.redis.service.RedisServiceImpl;
import org.englising.com.englisingbe.user.service.UserService;
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

    //TODO 반환 타입 결정 필요 (프론트와 상의)
    public MultiPlay createMultiPlay(MultiPlayRequestDto requestDto) {
        MultiPlay multiPlay = multiPlayRepository
                .save(MultiPlay.getMultiPlayFromMultiPlayRequestDto(requestDto, trackService.getRandomTrack(), createRandomImg()));
        redisService.saveMultiPlayGame(getMultiPlayGameFromMultiPlay(multiPlay));
        return multiPlay;
    }

    //TODO Redis 조회로 변경
    // Redis에 사용자 입장 처리
    public MultiPlayDetailResponseDto getMultiPlayById(Long multiPlayId, Long userId) {
        //TODO UserService findByUserId 추가 필요
//        redisService.addNewUserToMultiPlayGame(multiPlayId, );
        MultiPlayGame multiPlayGame = redisService.findMultiPlayGame(multiPlayId)
                .orElseThrow(() -> new GlobalException(ErrorHttpStatus.NO_MATCHING_MULTIPLAYGAME));
        return MultiPlayDetailResponseDto.builder()
                .multiPlayId(multiPlayGame.getMultiPlayId())
                .roomName(multiPlayGame.getRoomName())
                .multiPlayImgUrl(multiPlayGame.getMultiplayImgUrl())
                .genre(multiPlayGame.getGenre())
                .trackId(multiPlayGame.getTrackId())
                .currentUser(multiPlayGame.getUsers())
                .maxUser(multiPlayGame.getMaxUser())
                .isSecret(multiPlayGame.isSecret())
                .build();
    }

    //TODO 변경 함
//    public List<MultiPlayListResponseDto> getMultiPlayList(Genre genre, Integer page, Integer size) {
//        List<MultiPlay> multiPlays;
//        if(genre == null) {
//            multiPlays = multiPlayRepository.findAll();
//        } else {
//            multiPlays = multiPlayRepository.findByGenre(genre, PageRequest.of(page, size));
//        }
//        List<MultiPlayListResponseDto> multiPlayListResponseDto = new ArrayList<>();
//        for (MultiPlay multiPlay : multiPlays) {
//            MultiPlayListResponseDto dto = new MultiPlayListResponseDto();
//            dto.setMultiplayId(multiPlay.getMultiplayId());
//            dto.setRoomName(multiPlay.getRoomName());
//            dto.setMaxUser(multiPlay.getMaxUser());
//            dto.setMultiPlayImgUrl(multiPlay.getMultiPlayImgUrl());
//            dto.setGenre(multiPlay.getGenre());
//            multiPlayListResponseDto.add(dto);
//        }
//        return multiPlayListResponseDto;
//    }

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

    public Boolean getMultiPlayResult(Long multiplayId) {
        return multiPlayRepository.findByMultiplayId(multiplayId).getIsSecret();
    }

    private String createRandomImg(){
        return multiPlayImgRepository.getRandomImageUrl();
    }

    private MultiPlayGame getMultiPlayGameFromMultiPlay(MultiPlay multiPlay){
        return MultiPlayGame.builder()
                .multiPlayId(multiPlay.getMultiplayId())
                .trackId(multiPlay.getTrack().getTrackId())
                .maxUser(multiPlay.getMaxUser())
                .roomName(multiPlay.getRoomName())
                .genre(multiPlay.getGenre())
                .isSecret(multiPlay.getIsSecret())
                .roomPw(multiPlay.getRoomPw())
                .multiplayImgUrl(multiPlay.getMultiPlayImgUrl())
                .sentences(multiPlaySetterService.getMultiPlaySentenceListFromTrack(multiPlay.getTrack().getTrackId()))
                .users(new ArrayList<>())
                .round(1)
                .status(MultiPlayStatus.WAITING)
                .build();
    }
}