package org.englising.com.englisingbe.singleplay.entity;

import jakarta.persistence.*;
import org.englising.com.englisingbe.music.entity.Lyric;
import org.englising.com.englisingbe.word.entity.Word;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "singleplay_word")
public class SinglePlayWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "singleplay_word_id")
    private Long singlePlayWordId;

    @ManyToOne
    @JoinColumn(name = "singleplay_id", referencedColumnName = "singleplay_id")
    private SinglePlay singlePlay;

    @ManyToOne
    @JoinColumn(name = "word_id", referencedColumnName = "word_id")
    private Word word;

    @ManyToOne
    @JoinColumn(name = "lyric_id", referencedColumnName = "lyric_id")
    private Lyric lyric;

    @Column(name = "sentence_index")
    private int sentenceIndex;

    @Column(name = "word_index")
    private int wordIndex;

    @Column(name = "origin_word")
    private String originWord;

    @Column(name = "is_right")
    private Boolean isRight;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
