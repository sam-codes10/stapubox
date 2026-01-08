package com.example.stapubox.models.response;

import java.util.List;

import com.example.stapubox.entities.VenueEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllVenuesResponse {
    private boolean status;
    private String message;
    private List<VenueEntity> venueEntities;
}
