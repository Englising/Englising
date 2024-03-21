package org.englising.com.englisingbe.singleplay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.englising.com.englisingbe.music.dto.TrackAlbumArtistDto;

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

    public static TrackResponseDto getTrackResponseFromTrackAlbumArtist(TrackAlbumArtistDto trackAlbumArtistDto, Integer score, Boolean isLike){
        return TrackResponseDto.builder()
                .albumId(trackAlbumArtistDto.getAlbum().getAlbumId())
                .albumTitle(trackAlbumArtistDto.getAlbum().getTitle())
                .albumImg(trackAlbumArtistDto.getAlbum().getCoverImage())
                .trackId(trackAlbumArtistDto.getTrack().getTrackId())
                .trackTitle(trackAlbumArtistDto.getTrack().getTitle())
                .artists(trackAlbumArtistDto.getArtists()
                        .stream()
                        .map(artist -> {
                            return artist.getName();
                        }).collect(Collectors.toList()))
                .spotifyId(trackAlbumArtistDto.getTrack().getSpotifyId())
                .youtubeId(trackAlbumArtistDto.getTrack().getYoutubeId())
                .score(score)
                .isLike(isLike)
                .build();
    }
}
