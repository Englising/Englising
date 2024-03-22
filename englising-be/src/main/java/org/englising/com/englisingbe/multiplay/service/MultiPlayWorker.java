package org.englising.com.englisingbe.multiplay.service;

import org.englising.com.englisingbe.global.util.MultiPlayStatus;
import org.englising.com.englisingbe.global.util.WebSocketUrls;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlayGame;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlaySentence;
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
    private TaskScheduler scheduler;
    private SimpMessagingTemplate messagingTemplate;
    private RedisServiceImpl redisService;
    private MultiPlayGame multiPlayGame;
    private int bufferTime = 3000;
    private int inputTime = 10000;
    private int timer = 3000;
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
                        RoundDto.<List<MultiPlaySentence>>builder()
                                .round(multiPlayGame.getRound())
                                .status(MultiPlayStatus.ROUNDSTART)
                                .data(multiPlayGame.getSentences())
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
                                .data("3라운드 시작")
                                .build());
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
                        .status(MultiPlayStatus.MUSICSTART)
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
        //TODO 정답 확인 (맞은 경우 종료)
        // 정답이다 아니다만 보낸다
        System.out.println("roundResultAlert");
        messagingTemplate.convertAndSend(roundDestination,
                RoundDto.<String>builder()
                        .round(multiPlayGame.getRound())
                        .status(MultiPlayStatus.ROUNDEND)
                        .data(multiPlayGame.getRound()+"라운드 종료")
                        .build());
        // 1,2 라운드의 경우, 3초 뒤 라운드 시작 알림 전송
        if(multiPlayGame.getRound() < 3){
            multiPlayGame.setRound(multiPlayGame.getRound()+1);
            scheduleNextTask(this::sendRoundStartAlert, bufferTime);
        }
        else {
            shutdownScheduler();
        }
    }

    public void sendRandomHint(){
        System.out.println("sendRandomHint");
        //TODO
        // 3초 뒤 힌트 결과 공개 || 노래 재생 알림
        if(true){
            scheduleNextTask(this::sendHintResult, bufferTime);
        }
        else {
            scheduleNextTask(this::sendMusicStartAlert, bufferTime);
        }
    }

    public void sendHintResult(){
        System.out.println("sendHintResult");
        // 3초 뒤 입력 시작 알림
        scheduleNextTask(this::sendInputStartAlert, bufferTime);
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
            System.out.println("스케줄러가 종료되었습니다.");
        }
    }

    private int getTrackPlayTime(){
        BigDecimal startTime = multiPlayGame.getSentences().get(0).getStartTime();
        BigDecimal endTime = multiPlayGame.getSentences().get(multiPlayGame.getSentences().size() - 1).getEndTime();
        return endTime.subtract(startTime).multiply(new BigDecimal("1000")).intValue();
    }
}
