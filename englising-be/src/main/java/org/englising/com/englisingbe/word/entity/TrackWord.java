package org.englising.com.englisingbe.word.entity;

import jakarta.persistence.*;
import lombok.*;
import org.englising.com.englisingbe.music.entity.Lyric;
import org.englising.com.englisingbe.music.entity.Track;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_word_id")
    private Long trackWordId;

    @ManyToOne
    @JoinColumn(name = "track_id", referencedColumnName = "track_id")
    private Track track;

    @ManyToOne
    @JoinColumn(name = "lyric_id", referencedColumnName = "lyric_id")
    private Lyric lyric;

    @ManyToOne
    @JoinColumn(name = "word_id", referencedColumnName = "word_id")
    private Word word;

    @Column(name = "word_index")
    private Integer wordIndex;

    @Column(name = "origin_word")
    private String originWord;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
