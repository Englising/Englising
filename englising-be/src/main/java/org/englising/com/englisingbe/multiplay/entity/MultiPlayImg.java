package org.englising.com.englisingbe.multiplay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name = "multiplay_img")
@NoArgsConstructor
@AllArgsConstructor
public class MultiPlayImg {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "multiplay_img_id")
    private Long multiPlayImgId;

    @Column(name = "multiplay_img_url")
    private String multiPlayImgUrl;
}
