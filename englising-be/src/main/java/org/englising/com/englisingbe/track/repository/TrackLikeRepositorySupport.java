package org.englising.com.englisingbe.track.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.album.entity.QAlbum;
import org.englising.com.englisingbe.artist.entity.Artist;
import org.englising.com.englisingbe.artist.entity.QArtist;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.englising.com.englisingbe.global.util.PagingUtil;
import org.englising.com.englisingbe.singleplay.dto.TrackWithArtistDto;
import org.englising.com.englisingbe.track.entity.QArtistTrack;
import org.englising.com.englisingbe.track.entity.QTrack;
import org.englising.com.englisingbe.track.entity.QTrackLike;
import org.englising.com.englisingbe.track.entity.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class TrackLikeRepositorySupport {
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

    public Page<TrackWithArtistDto> findLikedTracksWithAlbumAndArtistByUserId(Long userId, Pageable pageable) {
        QTrackLike trackLike = QTrackLike.trackLike;
        QTrack track = QTrack.track;
        QAlbum album = QAlbum.album;
        QArtistTrack artistTrack = QArtistTrack.artistTrack;
        QArtist artist = QArtist.artist;

        List<Tuple> results = queryFactory
                .select(track, artist)
                .from(trackLike)
                .join(trackLike.track, track)
                .join(track.album, album)
                .leftJoin(artistTrack).on(artistTrack.track.eq(track))
                .leftJoin(artistTrack.artist, artist)
                .where(trackLike.user.userId.eq(userId)
                        .and(trackLike.isLiked.isTrue()))
                .fetch();


        Map<Track, List<Artist>> trackArtistsMap = new HashMap<>();
        results.forEach(tuple -> {
            Track t = tuple.get(track);
            Artist a = tuple.get(artist);
            trackArtistsMap.computeIfAbsent(t, k -> new ArrayList<>()).add(a);
        });


        List<TrackWithArtistDto> dtos = trackArtistsMap.entrySet().stream()
                .map(entry -> TrackWithArtistDto.builder()
                        .track(entry.getKey())
                        .artists(entry.getValue())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, dtos.size());
    }

    private BooleanExpression isLikedByUser(Long userId) {
        QTrackLike trackLike = QTrackLike.trackLike;
        return trackLike.user.userId.eq(userId).and(trackLike.isLiked.isTrue());
    }
}
