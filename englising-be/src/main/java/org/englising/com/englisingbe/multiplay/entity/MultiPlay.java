package org.englising.com.englisingbe.multiplay.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.englising.com.englisingbe.global.util.Genre;
import org.englising.com.englisingbe.multiplay.dto.request.MultiPlayRequestDto;
import org.englising.com.englisingbe.music.entity.Track;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
@Builder
@Table(name = "multiplay")
@NoArgsConstructor
@AllArgsConstructor
public class MultiPlay {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "multiplay_id")
    private Long multiplayId;

    @Column(name = "room_name")
    private String roomName;

    @ManyToOne
    @JoinColumn(name = "track_id", referencedColumnName = "track_id")
    private Track track;

    @Column(name = "max_user")
    private Integer maxUser;

    @Column(name = "genre")
    private Genre genre;

    @Column(name = "is_secret")
    private Boolean isSecret;

    @Column(name = "room_pw")
    private Integer roomPw;

    @Column(name = "multiplay_img_url")
    private String multiPlayImgUrl;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;


    public static MultiPlay getMultiPlayFromMultiPlayRequestDto(MultiPlayRequestDto requestDto, Track track, String multiPlayImgUrl) {
        return MultiPlay.builder()
                .roomName(requestDto.getRoomName())
                .track(track)
                .maxUser(requestDto.maxUser)
                .genre(requestDto.genre)
                .isSecret(requestDto.isSecret)
                .roomPw(requestDto.roomPw)
                .multiPlayImgUrl(multiPlayImgUrl)
                .build();
    }
}
