package org.englising.com.englisingbe.track.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.album.entity.QAlbum;
import org.englising.com.englisingbe.track.entity.QTrack;
import org.englising.com.englisingbe.track.entity.Track;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TracklRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public Track findTrackWithAlbumByTrackId(Long trackId){
        QTrack track = QTrack.track;
        QAlbum album = QAlbum.album;

        return queryFactory
                .selectFrom(track)
                .leftJoin(track.album, album).fetchJoin()
                .where(track.trackId.eq(trackId))
                .fetchOne();
    }


}
