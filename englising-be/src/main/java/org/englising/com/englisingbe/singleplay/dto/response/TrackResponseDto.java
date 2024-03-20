package org.englising.com.englisingbe.singleplay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.englising.com.englisingbe.singleplay.dto.TrackWithArtistDto;
import org.englising.com.englisingbe.artist.entity.Artist;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackResponseDto {
    public Long albumId;
    public String albumTitle;
    public String albumImg;
    public Long trackId;
    public String trackTitle;
    public List<String> artists;
    public String spotifyId;
    public String youtubeId;
    public Integer score;
    public Boolean isLike;

    public static TrackResponseDto getTrackResponseDtoFromTrack(TrackWithArtistDto trackWithArtist){
        return TrackResponseDto.builder()
                .albumId(trackWithArtist.getTrack().getAlbum().getAlbumId())
                .albumTitle(trackWithArtist.getTrack().getAlbum().getTitle())
                .albumImg(trackWithArtist.getTrack().getAlbum().getCoverImage())
                .trackId(trackWithArtist.getTrack().getTrackId())
                .trackTitle(trackWithArtist.getTrack().getTitle())
                .artists(trackWithArtist.getArtists().stream()
                        .map(Artist::getName)
                        .collect(Collectors.toList()))
                .spotifyId(trackWithArtist.getTrack().getSpotifyId())
                .youtubeId(trackWithArtist.getTrack().getYoutubeId())
                .score(0)
                .isLike(true)
                .build();
    }
}
