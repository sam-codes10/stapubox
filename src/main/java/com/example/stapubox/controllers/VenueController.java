package com.example.stapubox.controllers;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.stapubox.middleware.Auth;
import com.example.stapubox.models.payloads.JwtClaims;
import com.example.stapubox.models.payloads.VenuePayload;
import com.example.stapubox.models.response.AddVenueResponse;
import com.example.stapubox.models.response.AllVenuesResponse;
import com.example.stapubox.service.VenueService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/venue")
public class VenueController {
    private final VenueService venueService;
    private final Auth auth;

    public VenueController(VenueService venueService, Auth auth) {
        this.venueService = venueService;
        this.auth = auth;
    }

    @PostMapping("/add/admin")
    public ResponseEntity<?> addVenue(@RequestBody VenuePayload venuePayload,
            @RequestHeader("Authorization") String token) {
        // Remove Bearer prefix if present
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        JwtClaims claims = auth.parseToken(token);
        if (claims.getRole() == null || !claims.getRole().equals("ADMIN")) {
            return ResponseEntity.status(Response.SC_FORBIDDEN).body("Access denied");
        } else if (claims.getExpiry().before(new java.util.Date())) {
            return ResponseEntity.status(Response.SC_UNAUTHORIZED).body("Token has expired");
        }
        if (!isValidSlot(venuePayload.getSlotStartTime(), venuePayload.getSlotEndTime())) {
            return ResponseEntity.status(Response.SC_BAD_REQUEST)
                    .body("Invalid slot timings. Ensure 0000 <= start time < end time <= 2359");
        }

        AddVenueResponse response = venueService.addVenue(venuePayload);
        if (response.isStatus()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(Response.SC_INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllVenues() {
        AllVenuesResponse response = venueService.getAllVenues();
        if (response.isStatus()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(Response.SC_INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAllAvailableVenues(@RequestParam Long sportId, @RequestParam Long slot) {
        AllVenuesResponse response = venueService.getAllAvailableVenues(slot, sportId);
        if (response.isStatus()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(Response.SC_INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/delete/admin")
    public ResponseEntity<?> deleteVenue(@RequestParam Long venueId,
            @RequestHeader("Authorization") String token) {
        // Remove Bearer prefix if present
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        JwtClaims claims = auth.parseToken(token);
        if (claims.getRole() == null || !claims.getRole().equals("ADMIN")) {
            return ResponseEntity.status(Response.SC_FORBIDDEN).body("Access denied");
        } else if (claims.getExpiry().before(new java.util.Date())) {
            return ResponseEntity.status(Response.SC_UNAUTHORIZED).body("Token has expired");
        }

        try {
            venueService.softdeleteVenue(venueId);
            return ResponseEntity.ok("Venue deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(Response.SC_INTERNAL_SERVER_ERROR)
                    .body("Error during deleting venue: " + e.getMessage());
        }
    }

    private boolean isValidSlot(Long startTime, Long endTime) {

        if (startTime < 0 || startTime > 2359 || endTime < 0 || endTime > 2359) {
            return false;
        }

        Long startHour = startTime / 100;
        Long startMinute = startTime % 100;
        Long endHour = endTime / 100;
        Long endMinute = endTime % 100;

        if (startHour > 23 || startMinute > 59 || endHour > 23 || endMinute > 59) {
            return false;
        }

        if (startTime >= endTime) {
            return false;
        }

        return true;
    }

}
