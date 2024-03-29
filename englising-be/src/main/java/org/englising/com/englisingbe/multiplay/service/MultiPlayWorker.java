package org.englising.com.englisingbe.multiplay.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.englising.com.englisingbe.global.util.MultiPlayStatus;
import org.englising.com.englisingbe.global.util.WebSocketUrls;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlayGame;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlayStartInfo;
import org.englising.com.englisingbe.multiplay.dto.socket.HintAnswerDto;
import org.englising.com.englisingbe.multiplay.dto.socket.RoundDto;
import org.englising.com.englisingbe.multiplay.dto.socket.TimerDto;
import org.englising.com.englisingbe.redis.service.RedisServiceImpl;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class MultiPlayWorker {
    private MultiPlaySetterService multiPlaySetterService;

    private TaskScheduler scheduler;
    private SimpMessagingTemplate messagingTemplate;
    private RedisServiceImpl redisService;
    private MultiPlayGame multiPlayGame;
    private int bufferTime = 3000;
    private int inputTime = 30000;
    private int timer = 3000;
    private int hintResultWaitTime = 6000;
    private int trackPlayTime;
    private String roundDestination;
    private String timeDestination;

    public MultiPlayWorker(Long multiPlayId, SimpMessagingTemplate simpMessagingTemplate, RedisServiceImpl redisService){
        this.multiPlayGame = redisService.getMultiPlayGameById(multiPlayId);
        this.scheduler = new ThreadPoolTaskScheduler();
        this.messagingTemplate = simpMessagingTemplate;
        this.redisService = redisService;
        this.trackPlayTime = getTrackPlayTime();
        ((ThreadPoolTaskScheduler) scheduler).initialize();
        roundDestination = WebSocketUrls.roundUrl +multiPlayGame.getMultiPlayId().toString();
        timeDestination = WebSocketUrls.timeUrl+multiPlayGame.getMultiPlayId().toString();
    }

    public void sendRoundStartAlert() {
        switch (multiPlayGame.getRound()){
            case 1 :
                // 라운드 정보 업데이트
                this.multiPlayGame = redisService.updateRoundStatus(multiPlayGame.getMultiPlayId(), 1, MultiPlayStatus.ROUNDSTART);
                // 라운드 시작 알림 + 게임 정보 전송
                messagingTemplate.convertAndSend(roundDestination,
                        RoundDto.<MultiPlayStartInfo>builder()
                                .round(multiPlayGame.getRound())
                                .status(MultiPlayStatus.ROUNDSTART)
                                .data(MultiPlayStartInfo.builder()
                                        .trackTitle(multiPlayGame.getTrack().getTitle())
                                        .youtubeId(multiPlayGame.getTrack().getYoutubeId())
                                        .selectedHint(multiPlayGame.getSelectedHint())
                                        .beforeLyric(multiPlayGame.getBeforeLyric())
                                        .beforeLyricStartTime(multiPlayGame.getBeforeLyricStartTime())
                                        .afterLyric(multiPlayGame.getAfterLyric())
                                        .afterLyricEndTime(multiPlayGame.getAfterLyricEndTime())
                                        .sentences(multiPlayGame.getSentences())
                                        .build())
                                .build());
                // 노래 시작 알림 예약
                scheduleNextTask(this::sendMusicStartAlert, bufferTime);
                break;
            case 2:
                // 라운드 정보 업데이트
                this.multiPlayGame = redisService.updateRoundStatus(multiPlayGame.getMultiPlayId(), 2, MultiPlayStatus.ROUNDSTART);
                // 라운드 시작 알림
                messagingTemplate.convertAndSend(roundDestination,
                        RoundDto.<String>builder()
                                .round(multiPlayGame.getRound())
                                .status(MultiPlayStatus.ROUNDSTART)
                                .data("2라운드 시작")
                                .build());
                // 노래 시작 알림 예약
                scheduleNextTask(this::sendMusicStartAlert, bufferTime);
                break;
            case 3:
                // 라운드 정보 업데이트
                this.multiPlayGame = redisService.updateRoundStatus(multiPlayGame.getMultiPlayId(), 3, MultiPlayStatus.ROUNDSTART);
                // 라운드 시작 알림
                messagingTemplate.convertAndSend(roundDestination,
                        RoundDto.<String>builder()
                                .round(multiPlayGame.getRound())
                                .status(MultiPlayStatus.ROUNDSTART)
                                .data("3라운드 시작") // 선택된 힌트 결과를 ??
                                .build());
                // 힌트 결과 알림 예약 (3초 뒤)
                scheduleNextTask(this::sendHintResult, bufferTime);
                System.out.println("sendRoundStartAlert hint send"+multiPlayGame.getRound());
                break;
        }
    }

    public void sendMusicStartAlert() {
        // 라운드 정보 업데이트
        this.multiPlayGame = redisService.updateRoundStatus(multiPlayGame.getMultiPlayId(), multiPlayGame.getRound(), MultiPlayStatus.MUSICSTART);
        // 음악 시작 알림
        messagingTemplate.convertAndSend(roundDestination,
                RoundDto.<String>builder()
                        .round(multiPlayGame.getRound())
                        .status(MultiPlayStatus.MUSICSTART)
                        .data("음악 재생 시작")
                        .build());
        // 3초 단위로 타이머 알림 시작
        timerAlert(trackPlayTime);
        // 입력 시작 알림 예약
        scheduleNextTask(this::sendInputStartAlert, trackPlayTime);
    }

    public void sendInputStartAlert(){
        // 라운드 정보 업데이트
        this.multiPlayGame = redisService.updateRoundStatus(multiPlayGame.getMultiPlayId(), multiPlayGame.getRound(), MultiPlayStatus.INPUTSTART);
        // 입력 시작 알림
        messagingTemplate.convertAndSend(roundDestination,
                RoundDto.<String>builder()
                        .round(multiPlayGame.getRound())
                        .status(MultiPlayStatus.INPUTSTART)
                        .data("팀 답안 입력 시작")
                        .build());
        // 3초 단위로 타이머 알림 시작
        timerAlert(inputTime);
        // 입력 종료 알림 예약
        scheduleNextTask(this::sendInputEndAlert, inputTime);
    }

    public void sendInputEndAlert(){
        // 라운드 정보 업데이트
        this.multiPlayGame = redisService.updateRoundStatus(multiPlayGame.getMultiPlayId(), multiPlayGame.getRound(), MultiPlayStatus.INPUTEND);
        // 입력 시작 알림
        messagingTemplate.convertAndSend(roundDestination,
                RoundDto.<String>builder()
                        .round(multiPlayGame.getRound())
                        .status(MultiPlayStatus.INPUTEND)
                        .data("팀 답안 입력 종료")
                        .build());
        // 3초 뒤 결과 공개 알림 예약
        scheduleNextTask(this::roundResultAlert, bufferTime);
    }

    public void roundResultAlert(){
        // 정답 확인 및 결과 전송
        boolean isCorrect = wrongAnswerCount() == 0;
        messagingTemplate.convertAndSend(roundDestination,
                RoundDto.<Boolean>builder()
                        .round(multiPlayGame.getRound())
                        .status(MultiPlayStatus.ROUNDEND)
                        .data(isCorrect)
                        .build());
        // 1, 2 라운드에서 정답이 아닌 경우 -> 다음 라운드 시작 알림 예약
        if(multiPlayGame.getRound() < 3 && !isCorrect){
            scheduleNextTask(this::sendRoundStartAlert, bufferTime);
        }
        // 3 라운드이거나, 1,2라운드에서 정답인 경우 -> 게임 종료
        else {
            // Redis에서 게임 객체 삭제
            redisService.deleteMultiPlayGame(multiPlayGame.getMultiPlayId());
            // 스케줄러 종료
            shutdownScheduler();
        }
        multiPlayGame.setRound(multiPlayGame.getRound()+1);
    }

    public void sendHintResult(){
        this.multiPlayGame = redisService.updateRoundStatus(multiPlayGame.getMultiPlayId(), multiPlayGame.getRound(), MultiPlayStatus.HINT);
        // 랜덤 힌트에 따라 분기
        // 노래일 경우 -> 3초 뒤에 노래 시작 알림
        // 노래가 아닐 경우 -> 6초 뒤에 입력 시작 알림
        switch (multiPlayGame.getSelectedHint()){
            // HINT : 0.7 배속 듣기
            case 1:
                // 노래 재생 시간 *0.7 설정
                trackPlayTime = (int) (trackPlayTime*0.7);
                // 노래 재생 시작 알림 예약 (3초뒤)
                scheduleNextTask(this::sendMusicStartAlert, bufferTime);
                break;
            // HINT : 2 배속 듣기
            case 2:
                // 노래 재생 시간 *0.5 설정
                trackPlayTime = (int) (trackPlayTime*0.5);
                // 노래 재생 시작 알림 예약 (3초뒤)
                scheduleNextTask(this::sendMusicStartAlert, bufferTime);
                break;
            // HINT : 오답 수 확인
            case 3:
                // 오답 수 계산
                int wrongAnswerCount = wrongAnswerCount();
                messagingTemplate.convertAndSend(roundDestination,
                        RoundDto.<Integer>builder()
                                .round(multiPlayGame.getRound())
                                .status(MultiPlayStatus.HINTRESULT)
                                .data(wrongAnswerCount)
                                .build());
                // 6초 뒤 입력 시작 알림
                scheduleNextTask(this::sendInputStartAlert, hintResultWaitTime);
                break;
            // HINT : 랜덤 위치 정답 알파벳 5개 공개
            case 4:
                // 랜덤 알파벳 선정
                List<HintAnswerDto> hintResult = getRandomAnswerPositions();
                messagingTemplate.convertAndSend(roundDestination,
                        RoundDto.<List<HintAnswerDto>>builder()
                                .round(multiPlayGame.getRound())
                                .status(MultiPlayStatus.HINTRESULT)
                                .data(hintResult)
                                .build());
                // 6초 뒤 입력 시작 알림
                scheduleNextTask(this::sendInputStartAlert, hintResultWaitTime);
                break;
        }
    }

    private int wrongAnswerCount() {
        Map<Integer, String> userAnswerMap = multiPlayGame.getAnswerAlphabets();
        Map<Integer, String> correctAnswerMap = redisService.findMultiPlayAnswerMap(multiPlayGame.getMultiPlayId())
                .orElseThrow(()->new GlobalException(ErrorHttpStatus.NO_MATCHING_MULTIPLAYGAME));
        int wrongAnswerCount = 0;
        for (Map.Entry<Integer, String> entry : userAnswerMap.entrySet()) {
            int index = entry.getKey();
            String userAnswer = entry.getValue();
            String correctAnswer = correctAnswerMap.getOrDefault(index, "");
            if (!userAnswer.equals(correctAnswer)) {
                wrongAnswerCount++;
            }
        }
        return wrongAnswerCount;
    }

    private List<HintAnswerDto> getRandomAnswerPositions() {
        Map<Integer, String> answerMap = multiPlayGame.getAnswerAlphabets();

        List<Integer> answerIndexes = new ArrayList<>(answerMap.keySet());
        Collections.shuffle(answerIndexes);

        List<HintAnswerDto> answerList = new ArrayList<>();
        for (int i = 0; i < Math.min(5, answerIndexes.size()); i++) {
            int index = answerIndexes.get(i);
            String answer = answerMap.get(index);
            answerList.add(new HintAnswerDto(index, answer));
        }
        return answerList;
    }

    private void timerAlert(long duration) {
        TaskScheduler timerScheduler = new ThreadPoolTaskScheduler();
        ((ThreadPoolTaskScheduler) timerScheduler).initialize();

        long startTime = System.currentTimeMillis();
        long endTime = startTime + duration;

        timerScheduler.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime <= endTime) {
                long remainingTime = (endTime - currentTime)/1000;
                messagingTemplate.convertAndSend(timeDestination,
                        TimerDto.builder()
                                .message("남은 시간 타이머")
                                .leftTime(remainingTime)
                                .build());
            } else {
                if(timerScheduler instanceof ThreadPoolTaskScheduler) {
                    ((ThreadPoolTaskScheduler) timerScheduler).shutdown();
                }
            }
        }, timer);
    }

    private void scheduleNextTask(Runnable task, long delayInMillis) {
        scheduler.schedule(task, new Date(System.currentTimeMillis() + delayInMillis));
    }

    private void shutdownScheduler() {
        if (scheduler instanceof ThreadPoolTaskScheduler) {
            ((ThreadPoolTaskScheduler) scheduler).shutdown();
        }
    }

    private int getTrackPlayTime(){
        BigDecimal startTime = multiPlayGame.getSentences().get(0).getStartTime();
        BigDecimal endTime = multiPlayGame.getSentences().get(multiPlayGame.getSentences().size() - 1).getEndTime();
        return endTime.subtract(startTime).multiply(new BigDecimal("1000")).intValue();
    }
}
