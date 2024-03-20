package org.englising.com.englisingbe.track.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.album.entity.QAlbum;
import org.englising.com.englisingbe.global.util.PagingUtil;
import org.englising.com.englisingbe.track.entity.QTrack;
import org.englising.com.englisingbe.track.entity.QTrackLike;
import org.englising.com.englisingbe.track.entity.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.types.ExpressionUtils.orderBy;

@Repository
@RequiredArgsConstructor
public class TrackLikelRepositorySupport {
    private final JPAQueryFactory queryFactory;
    private final PagingUtil pagingUtil;

    public Page<Track> findLikedTracksWithAlbumByUserId(Long userId, Pageable pageable) {
        QTrackLike trackLike = QTrackLike.trackLike;
        QTrack track = QTrack.track;
        QAlbum album = QAlbum.album;

        JPQLQuery<Track> query = queryFactory
                .select(track)
                .from(trackLike)
                .join(trackLike.track, track)
                .join(track.album, album)
                .where(trackLike.user.userId.eq(userId).and(trackLike.isLiked.isTrue()))
                .orderBy(track.updatedAt.coalesce(track.createdAt).desc());
        return pagingUtil.getPageImpl(pageable, query, Track.class);
    }

    private BooleanExpression isLikedByUser(Long userId) {
        QTrackLike trackLike = QTrackLike.trackLike;
        return trackLike.user.userId.eq(userId).and(trackLike.isLiked.isTrue());
    }
}
