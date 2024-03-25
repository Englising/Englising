package org.englising.com.englisingbe.music.repository;

import org.englising.com.englisingbe.music.entity.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long> {
    @Query(value = "select * from track t where t.youtube_id is not null and exists (select 1 from lyric l where l.track_id = t.track_id) order by rand() limit 1", nativeQuery = true)
    Track findRandomTrackWithLyricsAndYoutubeId();

    @Query("SELECT t FROM Track t WHERE t.title LIKE %:keyword% AND t.youtubeId IS NOT NULL")
    Page<Track> findByTitleContainingAndYoutubeIdIsNotNull(@Param("keyword") String keyword, Pageable pageable);
}