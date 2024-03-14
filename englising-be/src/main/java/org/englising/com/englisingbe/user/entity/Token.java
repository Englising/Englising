package org.englising.com.englisingbe.user.entity;

import jakarta.persistence.*;
import lombok.*;

// todo. Redis 사용 전에 임시로 만든 테이블

@Builder
@Data
@Entity
@Table(name = "token")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long tokenId;

    @Column(name = "refresh_token")
    private String refreshToken;

    public void updateRefreshToken (String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

}
