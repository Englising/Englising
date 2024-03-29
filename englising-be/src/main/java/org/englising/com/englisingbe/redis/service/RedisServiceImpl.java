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

    public List<MultiPlayGame> getWaitingMultiPlayGames(Genre genre) {
        List<Object> games = redisTemplate.opsForValue().multiGet(
                redisTemplate.keys(gamePrefix+"*")
        );
        if (genre.equals(Genre.all)){
            return games.stream()
                    .filter(MultiPlayGame.class::isInstance)
                    .map(MultiPlayGame.class::cast)
                    .filter(game -> game.getStatus() == MultiPlayStatus.WAITING)
                    .toList();
        }
        return games.stream()
                .filter(MultiPlayGame.class::isInstance)
                .map(MultiPlayGame.class::cast)
                .filter(game -> game.getStatus() == MultiPlayStatus.WAITING && game.getGenre() == genre)
                .toList();
    }

    public boolean addNewUserToMultiPlayGame(Long multiPlayId, MultiPlayUser user) {
        MultiPlayGame game = getMultiPlayGameById(multiPlayId);
        List<MultiPlayUser> users = game.getUsers();
        if (users == null) {
            users = new ArrayList<>();
            game.setUsers(users);
        }
        // 이미 방 정원이 다 찬 경우
        if(users.size() >= game.getMaxUser()){
            return false;
        }
        // 방에 이미 참여 중인 사람인 경우
        boolean userExists = users.stream()
                .anyMatch(existingUser -> existingUser.getUserId().equals(user.getUserId()));

        if (userExists) {
            throw new GlobalException(ErrorHttpStatus.USER_ALREADY_EXISTS);
        }
        // 에러가 없을 경우 유저 추가 및 게임 상태 저장
        game.getUsers().add(user);
        saveMultiPlayGame(game);
        return true;
    }

    public boolean deleteUserToMultiPlayGame(Long multiPlayId, MultiPlayUser user) {
        boolean result = false;
        MultiPlayGame game = getMultiPlayGameById(multiPlayId);
        List<MultiPlayUser> users = game.getUsers();
        MultiPlayUser userToRemove = null;
        for (MultiPlayUser currentUser : users) {
            if (currentUser.getUserId().equals(user.getUserId())) {
                userToRemove = currentUser;
                result = true;
                break;
            }
        }
        if (userToRemove != null) {
            users.remove(userToRemove);
        }
        game.setUsers(users);
        saveMultiPlayGame(game);
        return result;
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
        answerMap.put(answerDto.getAlphabetIndex(), answerDto.getAlphabet());
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

    public void deleteMultiPlayGame(Long multiPlayId){
        redisTemplate.delete(gamePrefix + multiPlayId);
    }
}
