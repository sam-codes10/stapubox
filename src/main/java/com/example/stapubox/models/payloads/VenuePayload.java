package com.example.stapubox.models.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenuePayload {
    private String venueName;
    private String venueLocation;
    private Long sportId;
    private Long slotStartTime;
    private Long slotEndTime;
}
