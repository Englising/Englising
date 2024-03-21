package org.englising.com.englisingbe.user.entity;

import jakarta.persistence.*;
import lombok.*;


@Builder
@Data
@Entity
@Table(name = "profile_img")
@NoArgsConstructor
@AllArgsConstructor
public class ProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_img_id")
    private Long profileImageId;

    @Column(name = "profile_img_url")
    private String profileImageUrl;

}


