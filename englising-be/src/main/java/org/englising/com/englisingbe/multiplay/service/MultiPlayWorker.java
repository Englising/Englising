package org.englising.com.englisingbe.multiplay.service;

import org.englising.com.englisingbe.multiplay.dto.gameresponse.MultiPlayGameDefaultResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Date;

public class MultiPlayWorker {

    //TODO (Controller)
    // 1. 게임 시작 요청 (Controller Get)
    // 방장인지 아닌지 확인하기
    // worker 생성하기
    // trackId, leftTime(+3sec), sentences (list(starttime, endtime, {index, word})), youtubeId, type(RoundStart), round
    // 시작 알림 송신 + 노래 정보 보내주기 + 타이머 시작

    //TODO (Worker)
    // 1. Worker 생성 시 (1라운드 시작)
    // 3초 뒤 노래 재생 알림 -> sendMusicStartAlert
    // 노래 시간 종료 뒤 입력 시작 알림 -> sendInputStartAlert
    // 입력 시간 종료 뒤 입력 종료 알림 -> sendInputEndAlert
    // 3초 뒤 답안 공개 -> roundResultAlert


    public static void main(String[] args) {
        MultiPlayWorker multiPlayWorker = new MultiPlayWorker();
    }
    private TaskScheduler scheduler;
    private SimpMessagingTemplate messagingTemplate;
    private MultiPlayGameDefaultResponse multiPlayGameInfo;
    private int bufferTime = 3000;
    private int inputTime = 10000;
    private int timer = 3000;
    private int round = 1;
    private int trackPlayTime;
    public MultiPlayWorker(){
        this.scheduler = new ThreadPoolTaskScheduler();
//        this.messagingTemplate = messagingTemplate;
        ((ThreadPoolTaskScheduler) scheduler).initialize();
        sendMusicStartAlert();
    }

    private void sendRoundStartAlert() {
        // 3라운드 분기
        if(round == 3){
            System.out.println("sendRoundStartAlert hint send"+round);
        }
        else {
            System.out.println("sendRoundStartAlert"+round);
            // 라운드 시작 알림 3초 뒤로 예약
            scheduleNextTask(this::sendMusicStartAlert, bufferTime);
        }
    }

    public void sendMusicStartAlert() {
        System.out.println("sendMusicStartAlert");
        // 3초 단위로 타이머 시작
        timerAlert(trackPlayTime);
        // 노래 재생 후 입력 시작 알림 예약
        scheduleNextTask(this::sendInputStartAlert, trackPlayTime);
    }
    public void sendInputStartAlert(){
        System.out.println("sendInputStartAlert");
        // 3초 단위로 타이머 시작
        timerAlert(trackPlayTime);
        // input 시간 후 입력 종료 알림 예약
        scheduleNextTask(this::sendInputEndAlert, trackPlayTime);
    }

    public void sendInputEndAlert(){
        System.out.println("sendInputEndAlert");
        // 3초 뒤 결과 공개 알림 예약
        scheduleNextTask(this::roundResultAlert, bufferTime);
    }

    public void roundResultAlert(){
        //TODO 정답 확인
        // 정답이다 아니다만 보낸다
        System.out.println("roundResultAlert");
        // 1,2 라운드의 경우, 3초 뒤 라운드 시작 알림 전송
        if(round < 3){
            round++;
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

    // 3초 간격으로 보내는 타이머
    private void timerAlert(long duration) {
        TaskScheduler timerScheduler = new ThreadPoolTaskScheduler();
        ((ThreadPoolTaskScheduler) timerScheduler).initialize();

        long startTime = System.currentTimeMillis();
        long endTime = startTime + duration;

        timerScheduler.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime <= endTime) {
                long remainingTime = (endTime - currentTime)/1000;
                System.out.println("Remaining time: " + remainingTime + " ms");
            } else {
                System.out.println("Task completed.");
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

    private void sendMessage(String destination, String message){
        messagingTemplate.convertAndSend(destination, message);
    }
}
