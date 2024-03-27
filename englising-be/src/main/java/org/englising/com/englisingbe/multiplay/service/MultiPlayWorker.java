package org.englising.com.englisingbe.multiplay.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import org.englising.com.englisingbe.global.util.MultiPlayStatus;
import org.englising.com.englisingbe.global.util.WebSocketUrls;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlayGame;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlaySentence;
import org.englising.com.englisingbe.multiplay.dto.socket.HintAnswerDto;
import org.englising.com.englisingbe.multiplay.dto.socket.RoundDto;
import org.englising.com.englisingbe.multiplay.dto.socket.RoundResultDto;
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
        // 정답 확인
        boolean isCorrect = checkAnswer(multiPlayGame);

        // 정답 확인 후 처리
        if (isCorrect) {
            // 정답
            messagingTemplate.convertAndSend(roundDestination,
                RoundDto.<RoundResultDto>builder()
                    .round(multiPlayGame.getRound())
                    .status(MultiPlayStatus.ROUNDEND)
                    .data(RoundResultDto.builder()
                        .round(multiPlayGame.getRound())
                        .isCorrect(true)
                        .build())
                    .build());
            System.out.println("roundResultAlert");
        } else {
            // 오답
            messagingTemplate.convertAndSend(roundDestination,
                RoundDto.<RoundResultDto>builder()
                    .round(multiPlayGame.getRound())
                    .status(MultiPlayStatus.ROUNDEND)
                    .data(RoundResultDto.builder()
                        .round(multiPlayGame.getRound())
                        .isCorrect(false)
                        .build())
                    .build());
            System.out.println("roundResultAlert");
        }

        // 1,2 라운드의 경우, 3초 뒤 라운드 시작 알림 전송
        if (multiPlayGame.getRound() == 3 || isCorrect) {
            shutdownScheduler(); // 게임 종료
        } else if (multiPlayGame.getRound() < 3) {
            multiPlayGame.setRound(multiPlayGame.getRound() + 1);
            scheduleNextTask(this::sendRoundStartAlert, bufferTime);
        }
    }

    private boolean checkAnswer(MultiPlayGame multiPlayGame) {
        // 사용자의 입력
        Map<Integer, String> userAnswerMap = multiPlaySetterService.getAnswerInputMapFromMultiPlaySentenceList(multiPlayGame.getSentences(), true);
        // 실제 정답
        Map<Integer, String> correctAnswerMap = multiPlaySetterService.getAnswerInputMapFromMultiPlaySentenceList(multiPlayGame.getSentences(), false);
        // 사용자의 입력과 실제 정답을 비교하여 정답 여부 확인
        return userAnswerMap.equals(correctAnswerMap);
    }

    public void sendRandomHint(){
        // 1부터 4까지의 랜덤한 숫자 생성
        Random random = new Random();
        int randomHintId = random.nextInt(4) + 1;

        if (randomHintId <= 2) {
            // 3초 뒤 노래 재생 알림 전송
            scheduleNextTask(this::sendMusicStartAlert, bufferTime);
        } else {

            if (randomHintId == 3) {
                // 결과값 계산
                boolean isCorrect = checkAnswer(multiPlayGame);
                // 오답 수 계산
                int wrongAnswerCount = wrongAnswerCount(multiPlayGame);
                // 오답 수 전송
                messagingTemplate.convertAndSend(roundDestination,
                    RoundDto.<Integer>builder()
                        .round(multiPlayGame.getRound())
                        .status(MultiPlayStatus.HINT)
                        .data(wrongAnswerCount)
                        .build());
            } else if (randomHintId == 4) {
                // 정답 중 랜덤으로 5개 위치의 위치 index와 그 자리 정답 알파벳을 리스트에 담아 전송
                List<HintAnswerDto> answerIndexes = getRandomAnswerPositions(multiPlayGame);
                messagingTemplate.convertAndSend(roundDestination,
                    RoundDto.<List<HintAnswerDto>>builder()
                        .round(multiPlayGame.getRound())
                        .status(MultiPlayStatus.HINT)
                        .data(answerIndexes)
                        .build());
            }
        }
        messagingTemplate.convertAndSend(roundDestination,
            RoundDto.<Integer>builder()
                .round(multiPlayGame.getRound())
                .status(MultiPlayStatus.HINT)
                .data(randomHintId)
                .build());
        System.out.println("sendRandomHint");
    }

    public int wrongAnswerCount(MultiPlayGame multiPlayGame) {
        // 사용자의 입력 알파벳 맵 가져오기
        Map<Integer, String> userAnswerMap = multiPlaySetterService.getAnswerInputMapFromMultiPlaySentenceList(multiPlayGame.getSentences(), true);
        // 실제 정답 알파벳 맵 가져오기
        Map<Integer, String> correctAnswerMap = multiPlaySetterService.getAnswerInputMapFromMultiPlaySentenceList(multiPlayGame.getSentences(), false);

        // 오답 수 계산
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

    public List<HintAnswerDto> getRandomAnswerPositions(MultiPlayGame multiPlayGame) {
        // 정답 알파벳 맵 가져오기
        Map<Integer, String> answerMap = multiPlaySetterService.getAnswerInputMapFromMultiPlaySentenceList(multiPlayGame.getSentences(), false);
        // 정답 위치의 리스트 생성
        List<Integer> answerIndexes = new ArrayList<>(answerMap.keySet());
        Collections.shuffle(answerIndexes);

        // 랜덤 위치에서 알파벳을 추출하여 HintAnswerDto 리스트에 추가
//        List<HintAnswerDto> answerList = new ArrayList<>();
//        for (int i = 0; i < Math.min(5, answerIndexes.size()); i++) {
//            int randomIndex = multiPlaySetterService.getRandomIndex();
//            int index = answerIndexes.get(i);
//            String answer = answerMap.get(index);
//            answerList.add(new HintAnswerDto(randomIndex, answer));
//        }
//        return answerList;
        // 정답 중 랜덤으로 5개 위치의 위치 index와 그 자리 정답 알파벳을 리스트에 담기
        List<HintAnswerDto> answerList = new ArrayList<>();
        Random random = new Random();
        List<Integer> indexes = new ArrayList<>(answerMap.keySet());
        Collections.shuffle(indexes);
        for (int i = 0; i < Math.min(5, indexes.size()); i++) {
            int index = indexes.get(i);
            String answer = answerMap.get(index);
            answerList.add(new HintAnswerDto(index, answer));
        }
        return answerList;
    }

    public void sendHintResult(){
        //TODO
        System.out.println("sendHintResult");
        sendRandomHint();
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
        }
    }

    private int getTrackPlayTime(){
        BigDecimal startTime = multiPlayGame.getSentences().get(0).getStartTime();
        BigDecimal endTime = multiPlayGame.getSentences().get(multiPlayGame.getSentences().size() - 1).getEndTime();
        return endTime.subtract(startTime).multiply(new BigDecimal("1000")).intValue();
    }
}
