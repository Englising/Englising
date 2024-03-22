package org.englising.com.englisingbe.multiplay.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.redis.service.RedisServiceImpl;
import org.springframework.scheduling.annotation.Async;
import org.englising.com.englisingbe.multiplay.dto.socket.AnswerDto;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@RequiredArgsConstructor
public class AnswerQueueService {
    private final RedisServiceImpl redisService;
    private final Map<Long, LinkedBlockingQueue<AnswerDto>> gameQueues = new ConcurrentHashMap<>();

    public LinkedBlockingQueue<AnswerDto> createQueue(Long multiPlayId) {
        return gameQueues.computeIfAbsent(multiPlayId, k -> new LinkedBlockingQueue<>());
    }

    public void addToQueue(Long multiPlayId, AnswerDto request) {
        LinkedBlockingQueue<AnswerDto> queue = gameQueues.computeIfAbsent(multiPlayId, k -> new LinkedBlockingQueue<>());
        queue.add(request);
    }

    @Async
    public void processQueue(Long multiPlayId) {
        LinkedBlockingQueue<AnswerDto> queue = gameQueues.get(multiPlayId);
        if (queue == null) {
            return;
        }
        while (true) {
            try {
                AnswerDto request = queue.take();
                redisService.updateAlphabetInput(multiPlayId, request);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void cleanupQueue(Long gameId) {
        gameQueues.remove(gameId);
    }
}
