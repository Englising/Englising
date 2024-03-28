package org.englising.com.englisingbe.webclient.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.singleplay.dto.response.LyricDto;
import org.englising.com.englisingbe.singleplay.dto.response.TrackResponseDto;
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
                .uri(uriBuilder -> uriBuilder.path("/singleplay/game/{trackId}").build(trackId))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<LyricDto>>() {});
        return mono.block(); // 여기에서 block() 호출
    }

    public List<Long> getRecommendTrackByUserId(Long userId) {
        Mono<List<Long>> mono = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/singleplay/recommend/{userId}").build(userId))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Long>>() {});
        return mono.block();
    }
}
