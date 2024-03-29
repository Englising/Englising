package org.englising.com.englisingbe.multiplay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackWordFastApiDto {
    private Long track_id;
    private Long lyric_id;
    private Integer word_index;
    private Float importance;
    private Timestamp updated_at;
    private Long track_word_id;
    private Long word_id;
    private String origin_word;
    private Timestamp created_at;
}