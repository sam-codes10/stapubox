package com.example.stapubox.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.stapubox.entities.VenueEntity;
import com.example.stapubox.models.payloads.VenuePayload;
import com.example.stapubox.models.response.AddVenueResponse;
import com.example.stapubox.models.response.AllVenuesResponse;
import com.example.stapubox.repositoryDao.VenueRepository;

@Service
public class VenueService {
    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Transactional
    public AddVenueResponse addVenue(VenuePayload payload) {
        AddVenueResponse addVenueResponse = new AddVenueResponse();
        VenueEntity venueEntity = new VenueEntity();
        try {
            BeanUtils.copyProperties(payload, venueEntity);
            Long cnt = venueRepository.findExistingVenueId(payload.getVenueName(), payload.getVenueLocation());
            if (cnt != null && cnt > 0) {
                venueEntity.setVenueId(cnt);
            } else {
                venueEntity.setVenueId(Long.valueOf(venueRepository.findMaxVenueIdForUpdate() + 1));
            }

            venueEntity.setAvailable(true);
            venueEntity.setDeleted(false);
            VenueEntity dbRes = venueRepository.save(venueEntity);
            addVenueResponse.setMessage("Venue added successfully");
            addVenueResponse.setStatus(true);
            addVenueResponse.setVenueEntity(dbRes);
        } catch (DataIntegrityViolationException de) {
            addVenueResponse.setMessage("Venue with the same name, location, and sport already exists.");
            addVenueResponse.setStatus(false);
        } catch (Exception e) {
            addVenueResponse.setMessage("Error during adding venue: " + e.getMessage());
            addVenueResponse.setStatus(false);
        }

        return addVenueResponse;
    }

    public AllVenuesResponse getAllVenues() {
        AllVenuesResponse response = new AllVenuesResponse();
        try {
            List<VenueEntity> venueEntities = venueRepository.findAll();
            response.setStatus(true);
            response.setMessage("Data Retrieved suceessfully");
            response.setVenueEntities(venueEntities);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Error in fetching data with exception :" + e.getMessage());
        }
        return response;
    }

    public AllVenuesResponse getAllAvailableVenues(Long slot, Long sportId) {
        AllVenuesResponse response = new AllVenuesResponse();
        try {
            List<VenueEntity> venueEntities = venueRepository
                    .findAvailableNotDeletedBySlotAndSport(slot, sportId);
            response.setStatus(true);
            response.setMessage("Data Retrieved suceessfully");
            response.setVenueEntities(venueEntities);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Error in fetching data with exception :" + e.getMessage());
        }
        return response;
    }

    public void softdeleteVenue(Long venueId) {
        int rowsAffected = venueRepository.softDeleteByVenueId(venueId);
        if (rowsAffected == 0) {
            throw new RuntimeException("Venue with ID " + venueId + " not found.");
        }
    }
}
