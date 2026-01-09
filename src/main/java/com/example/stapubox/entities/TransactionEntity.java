package com.example.stapubox.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "booked_by_email")
    private String bookedByEmail;

    @Column(name = "booked_status")
    private boolean bookedStatus;

    @Column(name = "booked_at")
    private Date bookedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_record_booked", referencedColumnName = "record_id", nullable = false)
    private VenueEntity venue;
}
