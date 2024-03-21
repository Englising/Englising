package org.englising.com.englisingbe.track.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.album.entity.Album;
import org.englising.com.englisingbe.album.entity.QAlbum;
import org.englising.com.englisingbe.artist.entity.Artist;
import org.englising.com.englisingbe.artist.entity.QArtist;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.englising.com.englisingbe.track.dto.TrackAlbumArtistDto;
import org.englising.com.englisingbe.track.entity.QArtistTrack;
import org.englising.com.englisingbe.track.entity.QTrack;
import org.englising.com.englisingbe.track.entity.Track;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TrackRepositorySupport {
    private final JPAQueryFactory queryFactory;
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
                        .collect(Collectors.toList()))
                .build());
    }
}
