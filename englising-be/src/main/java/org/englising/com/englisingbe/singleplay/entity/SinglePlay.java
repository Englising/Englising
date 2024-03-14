package org.englising.com.englisingbe.singleplay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "singleplay")
public class SinglePlay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "singleplay_id")
    private Long singlePlayId;

    @Column(name = "singleplay_level_id")
    private Integer singlePlayLevelId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "track_id")
    private Long trackId;

    @Column(name = "score")
    private Integer score;

    @Column(name = "correct_rate")
    private Integer correctRate;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
