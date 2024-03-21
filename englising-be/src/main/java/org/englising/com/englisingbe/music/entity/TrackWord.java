package org.englising.com.englisingbe.music.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.englising.com.englisingbe.word.entity.Word;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "track_word")
public class TrackWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_word_id")
    private Long trackWordId;

    @ManyToOne
    @JoinColumn(name = "track_id", referencedColumnName = "track_id")
    private Track track;

    @ManyToOne
    @JoinColumn(name = "word_id", referencedColumnName = "word_id")
    private Word word;

    @Column(name = "sentence_num")
    private Integer sentenceNum;

    @Column(name = "sentence_seq")
    private String sentenceSeq;

    @Column(name = "importance")
    private Float importance;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
