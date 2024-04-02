package org.englising.com.englisingbe.music.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.global.util.Genre;
import org.englising.com.englisingbe.global.util.PagingUtil;
import org.englising.com.englisingbe.music.dto.TrackAlbumArtistDto;
import org.englising.com.englisingbe.music.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TrackRepositorySupport {
    private final JPAQueryFactory queryFactory;
    private final PagingUtil pagingUtil;
    private QTrack track = QTrack.track;
    private QAlbum album = QAlbum.album;
    private QArtist artist = QArtist.artist;
    private QArtistTrack artistTrack = QArtistTrack.artistTrack;

    public Optional<TrackAlbumArtistDto> findTrackWithAlbumAndArtistsByTrackId(Long trackId){
        List<Tuple> results = queryFactory
                .select(track, album, artist)
                .from(track)
                .join(track.album, album)
                .join(artistTrack).on(artistTrack.track.eq(track))
                .join(artistTrack.artist, artist)
                .where(track.trackId.eq(trackId))
                .fetch();
        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(TrackAlbumArtistDto.builder()
                .track(results.get(0).get(track))
                .album(results.get(0).get(album))
                .artists(results.stream()
                        .map(tuple -> tuple.get(artist))
                        .distinct()
                        .toList())
                .build());
    }

    public List<Track> findTracksByGenreAndLyricStatus(Genre genre, int limit){
        BooleanExpression genreCondition = genre.name().equals("all") ? null : QTrack.track.genre.eq(genre.name());
        BooleanExpression lyricsDoneCondition = QTrack.track.lyricStatus.eq("DONE");
        BooleanExpression youtubeIdNotNull = QTrack.track.youtubeId.isNotNull();
        BooleanExpression youtubeIdNotNone = QTrack.track.youtubeId.ne("NONE");

        BooleanExpression hasLyrics = JPAExpressions.selectOne()
                .from(QLyric.lyric)
                .where(QLyric.lyric.track.trackId.eq(QTrack.track.trackId))
                .exists();

        return queryFactory
                .selectFrom(QTrack.track)
                .where(youtubeIdNotNull, lyricsDoneCondition, genreCondition, hasLyrics, youtubeIdNotNone)
                .orderBy(QTrack.track.spotifyPopularity.desc())
                .limit(limit)
                .fetch();
    }


    public Page<Track> findByTitleContainingAndYoutubeIdIsNotNullAndOtherConditions(String keyword, Pageable pageable) {
        QTrack qTrack = QTrack.track;
        QLyric qLyric = QLyric.lyric;

        BooleanExpression titleCondition = qTrack.title.containsIgnoreCase(keyword);
        BooleanExpression youtubeIdCondition = qTrack.youtubeId.isNotNull().and(qTrack.youtubeId.ne("NONE"));
        BooleanExpression genreCondition = qTrack.genre.isNotNull();
        BooleanExpression lyricStatusCondition = qTrack.lyricStatus.eq("DONE");
        BooleanExpression koreanLyricCondition = qLyric.krText.isNotNull();

        JPAQuery<Track> query = queryFactory
                .selectFrom(qTrack)
                .leftJoin(qLyric)
                .on(qLyric.track.trackId.eq(qTrack.trackId).and(koreanLyricCondition))
                .where(titleCondition, youtubeIdCondition, genreCondition, lyricStatusCondition)
                .distinct()
                .orderBy(qTrack.spotifyPopularity.desc());

        return pagingUtil.getPageImpl(pageable, query, Track.class);
    }
}
