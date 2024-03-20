package org.englising.com.englisingbe.track.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.singleplay.dto.response.TrackResponseDto;
import org.englising.com.englisingbe.track.entity.TrackLike;
import org.englising.com.englisingbe.track.repository.TrackLikeRepository;
import org.englising.com.englisingbe.track.repository.TrackLikeRepositorySupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TrackLikeServiceImpl {
    private final TrackLikeRepository trackLikeRepository;
    private final TrackLikeRepositorySupport trackLikeRepositorySupport;

    public Page<TrackLike> getLikedTrackResponseDtoByUserId(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt", "createdAt"));
        return trackLikeRepository.getTrackLikeByUserUserId(userId, pageable);
    }
}
