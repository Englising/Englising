package org.englising.com.englisingbe.webclient.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.multiplay.dto.request.TrackWordFastApiDto;
import org.englising.com.englisingbe.singleplay.dto.response.LyricDto;
import org.englising.com.englisingbe.singleplay.dto.response.TrackResponseDto;
import org.englising.com.englisingbe.word.entity.TrackWord;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendApiService {
    private final WebClient webClient;

    public List<LyricDto> getSplittedWordsOfLyricByTrackId(Long trackId){
        Mono<List<LyricDto>> mono = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/singleplay/game/lyrics/{trackId}").build(trackId))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<LyricDto>>() {});
        return mono.block();
    }

    public List<Long> getRecommendTrackByUserId(Long userId) {
        Mono<List<Long>> mono = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/singleplay/playlist/recommend/{userId}").build(userId))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Long>>() {});
        return mono.block();
    }

    public List<TrackWordFastApiDto> getSinglePlayGameWords(Long trackId, Long userId, int level){
        Mono<List<TrackWordFastApiDto>> mono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/singleplay/game/words")
                        .queryParam("track-id", trackId)
                        .queryParam("user-id", userId)
                        .queryParam("level", level)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<TrackWordFastApiDto>>() {});
        return mono.block();
    }
}
