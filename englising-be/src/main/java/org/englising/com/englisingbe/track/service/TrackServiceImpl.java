package org.englising.com.englisingbe.track.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.track.repository.TrackRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackServiceImpl {
    private final TrackRepository trackRepository;


}
