package org.englising.com.englisingbe.redis.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.englising.com.englisingbe.global.util.Genre;
import org.englising.com.englisingbe.global.util.MultiPlayStatus;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlayGame;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlayUser;
import org.englising.com.englisingbe.multiplay.dto.socket.AnswerDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl {
    private final RedisTemplate<String, Object> redisTemplate;
    private final String gamePrefix = "multiplaygame:";

    private final String answerPrefix = "multiplayanswer:";
    public void saveMultiPlayGame(MultiPlayGame game) {
        redisTemplate.opsForValue().set(gamePrefix + game.getMultiPlayId(), game);
    }

    public Optional<MultiPlayGame> findMultiPlayGame(Long multiPlayId) {
        Object result = redisTemplate.opsForValue().get(gamePrefix + multiPlayId);
        if (result instanceof MultiPlayGame) {
            return Optional.of((MultiPlayGame) result);
        }
        return Optional.empty();
    }

    public List<MultiPlayGame> getWaitingMultiPlayGames(Genre genre, Integer page, Integer size) {
        List<Object> games = redisTemplate.opsForValue().multiGet(
                redisTemplate.keys(gamePrefix+"*")
        );
        return games.stream()
                .filter(MultiPlayGame.class::isInstance)
                .map(MultiPlayGame.class::cast)
                .filter(game -> game.getStatus() == MultiPlayStatus.WAITING && game.getGenre() == genre)
                .toList();
    }

    public void addNewUserToMultiPlayGame(Long multiPlayId, MultiPlayUser user) {
        MultiPlayGame game = getMultiPlayGameById(multiPlayId);
        List<MultiPlayUser> users = game.getUsers();
        if (users == null) {
            users = new ArrayList<>();
            game.setUsers(users);
        }
        boolean userExists = users.stream()
                .anyMatch(existingUser -> existingUser.getUserId().equals(user.getUserId()));

        if (userExists) {
            throw new GlobalException(ErrorHttpStatus.USER_ALREADY_EXISTS);
        }
        users.add(user);
        saveMultiPlayGame(game);
    }

    public MultiPlayGame updateRoundStatus(Long multiPlayId, int round, MultiPlayStatus status){
        MultiPlayGame game = getMultiPlayGameById(multiPlayId);
        game.setRound(round);
        game.setStatus(status);
        saveMultiPlayGame(game);
        return game;
    }

    public MultiPlayGame getMultiPlayGameById(Long multiPlayId){
        return findMultiPlayGame(multiPlayId)
                .orElseThrow(() -> new GlobalException(ErrorHttpStatus.NO_MATCHING_MULTIPLAYGAME));
    }

    public void saveAnswerMap(Long multiPlayId, Map<Integer, String> answerMap){
        redisTemplate.opsForValue().set(answerPrefix + multiPlayId, answerMap);
    }

    public boolean existsMultiPlayAnswerMap(Long multiPlayId) {
        Boolean exists = redisTemplate.hasKey(answerPrefix + multiPlayId);
        return exists != null && exists;
    }

    public void updateAlphabetInput(Long multiPlayId, AnswerDto answerDto){
        Map<Integer, String> answerMap = findMultiPlayAnswerMap(multiPlayId)
                .orElseThrow(()->new GlobalException(ErrorHttpStatus.NO_MATCHING_MULTIPLAYGAME));
        answerMap.put(answerDto.getAlphabetId(), answerDto.getAlphabet());
        System.out.println(answerMap);
        saveAnswerMap(multiPlayId, answerMap);
    }

    public Optional<Map<Integer, String>> findMultiPlayAnswerMap(Long multiPlayId) {
        Object result = redisTemplate.opsForValue().get(answerPrefix + multiPlayId);
        if (result instanceof Map) {
            Map<?, ?> rawMap = (Map<?, ?>) result;
            Map<Integer, String> answerMap = new HashMap<>();
            rawMap.forEach((key, value) -> {
                if (key instanceof String && value instanceof String) {
                    Integer keyAsInt = Integer.parseInt((String) key);
                    answerMap.put(keyAsInt, (String) value);
                }
            });
            return Optional.of(answerMap);
        }
        return Optional.empty();
    }
}