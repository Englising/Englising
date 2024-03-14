package org.englising.com.englisingbe.multiplay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "multiplay")
@NoArgsConstructor
@AllArgsConstructor
public class Multiplay {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "multiplay_id")
    private Long multiplayId;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "track_id")
    private Long trackId;

    @Column(name = "total_people")
    private Integer totalPeople;

    @Column(name = "genre")
    private String genre;

    @Column(name = "is_secret")
    private Boolean isSecret;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
