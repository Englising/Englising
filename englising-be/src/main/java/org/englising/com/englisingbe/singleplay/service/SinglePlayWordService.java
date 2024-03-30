package org.englising.com.englisingbe.singleplay.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.singleplay.repository.SinglePlayWordRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SinglePlayWordService {
    private final SinglePlayWordRepository singlePlayWordRepository;


}
