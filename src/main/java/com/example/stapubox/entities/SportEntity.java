package com.example.stapubox.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sports")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SportEntity {
    @Id
    @Column(name = "sport_id", unique = true, nullable = false)
    private Long sportId;

    @Column(name = "sport_name", nullable = false)
    private String sportName;

    @Column(name = "sport_code", unique = true, nullable = false)
    private String sportCode;

}
