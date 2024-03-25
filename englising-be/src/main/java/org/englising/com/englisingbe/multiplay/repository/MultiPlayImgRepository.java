package org.englising.com.englisingbe.multiplay.repository;

import org.englising.com.englisingbe.multiplay.entity.MultiPlayImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MultiPlayImgRepository extends JpaRepository<MultiPlayImg, Long> {
    @Query(value = "select multiplay_img_url from multiplay_img order by RAND() limit 1", nativeQuery = true)
    String getRandomImageUrl();
}