package org.englising.com.englisingbe.multiplay.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.multiplay.entity.MultiPlay;
import org.englising.com.englisingbe.multiplay.repository.MultiPlayRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MultiPlayServiceImpl {
    private final MultiPlayRepository multiPlayRepository;

    public List<MultiPlay> getMultiPlayList(String genre, Integer page, Integer size) {

        return null;
    }

    public MultiPlay createMultiPlay(MultiPlay multiPlay) {
        return multiPlayRepository.save(multiPlay);
    }

    public MultiPlay getMultiPlayById(Long multiPlayId) {
        return multiPlayRepository.findByMultiplayId(multiPlayId);
    }




}
