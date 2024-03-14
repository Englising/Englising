package org.englising.com.englisingbe.singleplay.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "singleplay_hint")
public class SinglePlayHint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "singleplay_level_id")
    private Integer singlepleyLevelId;

    @Column(name = "singleplay_level")
    private String singleplayLevel;

    @Column(name = "hint_cnt")
    private Integer hintCnt;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
