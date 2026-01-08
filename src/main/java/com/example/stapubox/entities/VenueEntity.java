package com.example.stapubox.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "venues", uniqueConstraints = {
        @UniqueConstraint(name = "uk_venue_unique", columnNames = { "venue_name", "venue_location", "sport_id" })
})
public class VenueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordID;

    @Column(name = "venue_id")
    private Long venueId;

    @Column(name = "venue_name")
    private String venueName;

    @Column(name = "venue_location")
    private String venueLocation;

    @Column(name = "sport_id")
    private Long sportId;

    @Column(name = "slot_start_time")
    private Long slotStartTime;

    @Column(name = "slot_end_time")
    private Long slotEndTime;

    @Column(name = "available")
    private boolean available;

    @Column(name = "deleted")
    private boolean deleted;
}
