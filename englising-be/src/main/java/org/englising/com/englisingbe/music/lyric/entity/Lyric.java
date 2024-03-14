package org.englising.com.englisingbe.music.lyric.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.englising.com.englisingbe.music.album.entity.Album;
import org.englising.com.englisingbe.music.track.entity.Track;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lyric")
public class Lyric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lyric_id")
    private Long albumId;

    @ManyToOne
    @JoinColumn(name = "track_id", referencedColumnName = "track_id")
    private Track track;

    @Column(name = "start_time", precision = 10, scale = 3)
    private BigDecimal startTime;

    @Column(name = "end_time", precision = 10, scale = 3)
    private BigDecimal endTime;

    @Column(name = "en_text", length = 512)
    private String enText;

    @Column(name = "kr_text", length = 512)
    private String krText;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
