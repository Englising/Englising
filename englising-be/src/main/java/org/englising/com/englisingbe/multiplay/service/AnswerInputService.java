package org.englising.com.englisingbe.multiplay.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.englising.com.englisingbe.global.util.MultiPlayStatus;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlayGame;
import org.englising.com.englisingbe.multiplay.dto.socket.AnswerDto;
import org.englising.com.englisingbe.redis.service.RedisServiceImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerInputService {
    private final RedisServiceImpl redisService;
    private final AnswerQueueService answerQueueService;

    public void updateAnswer(Long multiPlayId, AnswerDto answerDto){
        // AnswerMap의 존재 유무 확인
        if(!redisService.existsMultiPlayAnswerMap(multiPlayId)){
            throw new GlobalException(ErrorHttpStatus.NO_MATCHING_MULTIPLAYGAME);
        }
        // MultiPlay가 INPUTSTART 상태인지 확인, 해당 user가 해당 게임의 플레이어인지 확인
        MultiPlayGame multiPlayGame = redisService.getMultiPlayGameById(multiPlayId);
        //TODO 주석 해제
//        if(!multiPlayGame.getStatus().equals(MultiPlayStatus.INPUTSTART)
//                || multiPlayGame.getUsers().stream()
//                .noneMatch(user -> user.getUserId().equals(userId))){
//            throw new GlobalException(ErrorHttpStatus.NO_MATCHING_MULTIPLAYGAME);
//        }
        answerQueueService.addToQueue(multiPlayId, answerDto);
    }
}
