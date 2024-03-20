package org.englising.com.englisingbe.singleplay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.englising.com.englisingbe.artist.entity.Artist;
import org.englising.com.englisingbe.track.entity.Track;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackWithArtistDto {
    private Track track;
    private List<Artist> artists;
}
