package org.englising.com.englisingbe.track.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.englising.com.englisingbe.album.entity.Album;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "track")
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_id")
    private Long trackId;

    @ManyToOne
    @JoinColumn(name = "album_id", referencedColumnName = "album_id")
    private Album album;

    @Column(name = "track_index")
    private Integer trackIndex;

    @Column(name = "title")
    private String title;

    @Column(name = "spotify_id")
    private String spotifyId;

    @Column(name = "youtube_id")
    private String youtubeId;

    @Column(name = "isrc")
    private String isrc;

    @Column(name = "spotify_popularity")
    private String spotifyPopularity;

    @Column(name = "duration_ms")
    private Integer durationMs;

    @Column(name = "feature_acousticness")
    private Float featureAcousticness;

    @Column(name = "feature_danceability")
    private Float featureDancebility;

    @Column(name = "feature_energy")
    private Float featureEnergy;

    @Column(name = "feature_positiveness")
    private Float featurePositiveness;

    @Column(name = "genre")
    private String genre;

    @Column(name = "lyric_status")
    private String lyricStatus;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
