package com.example.stapubox.repositoryDao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.stapubox.entities.VenueEntity;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;

public interface VenueRepository extends JpaRepository<VenueEntity, Long> {

    @Query("SELECT v.venueId FROM VenueEntity v WHERE v.venueName = ?1 AND v.venueLocation = ?2")
    Long findExistingVenueId(String venueName, String location);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT COALESCE(MAX(v.venueId), 0) FROM VenueEntity v")
    Integer findMaxVenueIdForUpdate();

    List<VenueEntity> findByAvailable(boolean available);

    @Query("SELECT v FROM VenueEntity v WHERE v.available = true AND v.deleted = false AND v.slotStartTime <= ?1 AND v.slotEndTime >= ?1 AND v.sportId = ?2")
    List<VenueEntity> findAvailableNotDeletedBySlotAndSport(Long slot, Long sportId);

    @Modifying
    @Transactional
    @Query("UPDATE VenueEntity v SET v.deleted = true WHERE v.venueId = ?1")
    int deleteByVenueId(Long venueId);

}
