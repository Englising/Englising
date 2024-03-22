package org.englising.com.englisingbe.redis.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.englising.com.englisingbe.global.util.Genre;
import org.englising.com.englisingbe.global.util.MultiPlayStatus;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlayGame;
import org.englising.com.englisingbe.multiplay.dto.game.MultiPlayUser;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl {
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveMultiPlayGame(MultiPlayGame game) {
        redisTemplate.opsForValue().set("multiplaygame:" + game.getMultiPlayId(), game);
    }

    public Optional<MultiPlayGame> findMultiPlayGame(Long multiPlayId) {
        Object result = redisTemplate.opsForValue().get("multiplaygame:" + multiPlayId);
        if (result instanceof MultiPlayGame) {
            return Optional.of((MultiPlayGame) result);
        }
        return Optional.empty();
    }

    public List<MultiPlayGame> getWaitingMultiPlayGames(Genre genre, Integer page, Integer size) {
        List<Object> games = redisTemplate.opsForValue().multiGet(
                redisTemplate.keys("multiplaygame:*")
        );
        return games.stream()
                .filter(MultiPlayGame.class::isInstance)
                .map(MultiPlayGame.class::cast)
                .filter(game -> game.getStatus() == MultiPlayStatus.WAITING && game.getGenre() == genre)
                .toList();
    }

    public void addNewUserToMultiPlayGame(Long multiPlayId, MultiPlayUser user) {
        MultiPlayGame game = findMultiPlayGame(multiPlayId)
                .orElseThrow(() -> new GlobalException(ErrorHttpStatus.NO_MATCHING_MULTIPLAYGAME));
        List<MultiPlayUser> users = game.getUsers();
        if (users == null) {
            users = new ArrayList<>();
            game.setUsers(users);
        }
        users.add(user);
        saveMultiPlayGame(game);
    }
}
