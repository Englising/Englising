package org.englising.com.englisingbe.multiplay.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "multiplay")
@NoArgsConstructor
@AllArgsConstructor
public class Multiplay {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long multiplayId;
}
