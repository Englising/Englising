package org.englising.com.englisingbe;

import java.util.List;
import org.englising.com.englisingbe.music.entity.Track;
import org.englising.com.englisingbe.music.service.TrackServiceImpl;
import org.englising.com.englisingbe.singleplay.dto.response.TrackResponseDto;
import org.englising.com.englisingbe.singleplay.service.SinglePlayServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EnglisingBeApplicationTests {

    @Autowired
    TrackServiceImpl trackService;

    @Autowired
    SinglePlayServiceImpl service;

    @Test
    void contextLoads() {
        List<TrackResponseDto> track = service.getRecommendedTracks(1L);
        System.out.println(track);
    }

}
