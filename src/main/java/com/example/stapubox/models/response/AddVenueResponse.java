package com.example.stapubox.models.response;

import com.example.stapubox.entities.VenueEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddVenueResponse {
    private String message;
    private boolean status;
    private VenueEntity venueEntity;
}
