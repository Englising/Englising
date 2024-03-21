package org.englising.com.englisingbe.music.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.englising.com.englisingbe.music.entity.Album;
import org.englising.com.englisingbe.music.entity.Artist;
import org.englising.com.englisingbe.music.entity.Track;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackAlbumArtistDto {
    public Track track;
    public Album album;
    public List<Artist> artists;
}
