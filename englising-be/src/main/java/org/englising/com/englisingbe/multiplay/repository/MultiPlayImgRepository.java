package org.englising.com.englisingbe.multiplay.repository;

import org.englising.com.englisingbe.multiplay.entity.MultiPlayImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MultiPlayImgRepository extends JpaRepository<MultiPlayImg, Long> {
    @Query(value = "SELECT multiplay_img_url FROM multiplay_img ORDER BY RAND() LIMIT 1", nativeQuery = true)
    String getRandomImageUrl();
}