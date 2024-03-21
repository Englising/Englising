package org.englising.com.englisingbe.like.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.global.util.PagingUtil;
import org.englising.com.englisingbe.track.entity.QTrackLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TrackLikeRepositorySupport {
    private final JPAQueryFactory queryFactory;
    private final PagingUtil pagingUtil;

    public Page<Long> findLikedTrackIdsByUserId(Long userId, Pageable pageable) {
        QTrackLike trackLike = QTrackLike.trackLike;
        JPQLQuery<Long> query = queryFactory
                .select(trackLike.track.trackId)
                .from(trackLike)
                .where(trackLike.user.userId.eq(userId)
                        .and(trackLike.isLiked.isTrue()));
        return pagingUtil.getPageImpl(pageable, query, Long.class);
    }

    private BooleanExpression isLikedByUser(Long userId) {
        QTrackLike trackLike = QTrackLike.trackLike;
        return trackLike.user.userId.eq(userId).and(trackLike.isLiked.isTrue());
    }
}
