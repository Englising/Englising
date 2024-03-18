package org.englising.com.englisingbe.singleplay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayListResponseDto {
    public Long albumId;
    public String albumTitle;
    public Long trackId;
    public String trackTitle;
    public List<String> artists;
    public String spotifyId;
    public String youtubeId;
    public Integer score;
    public Boolean isLike;
}
