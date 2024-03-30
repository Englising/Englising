package org.englising.com.englisingbe.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@Entity
@Table(name = "profile_color")
@NoArgsConstructor
@AllArgsConstructor
public class ProfileColor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_color_id")
    private Long profileColorId;

    @Column(name = "profile_color")
    private String profileColor;

}

