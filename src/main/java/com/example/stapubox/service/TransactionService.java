package com.example.stapubox.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stapubox.entities.TransactionEntity;
import com.example.stapubox.entities.VenueEntity;
import com.example.stapubox.models.response.BookingResponse;
import com.example.stapubox.models.response.CancelBookingResponse;
import com.example.stapubox.repositoryDao.TransactionRepository;
import com.example.stapubox.repositoryDao.VenueRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private VenueRepository venueRepository;

    @Transactional
    public BookingResponse bookVenue(Long recordId, String bookedByEmail) {
        BookingResponse bookingResponse = new BookingResponse();
        try {
            Optional<VenueEntity> venueEntity = venueRepository.findById(recordId);
            if (venueEntity.isEmpty()) {
                bookingResponse.setTransactionId(null);
                bookingResponse.setBookedStatus(false);
                bookingResponse.setVenueRecordBooked(null);
                bookingResponse.setMessage("No venue entity of record id : " + recordId.toString());
                return bookingResponse;
            }
            VenueEntity venueEntityTobeSave = venueEntity.get();
            if (!venueEntityTobeSave.isAvailable()) {
                bookingResponse.setTransactionId(null);
                bookingResponse.setBookedStatus(false);
                bookingResponse.setVenueRecordBooked(null);
                bookingResponse.setMessage("Venue is not available for booking for record id : " + recordId.toString());
                return bookingResponse;
            }
            venueEntityTobeSave.setAvailable(false);
            venueRepository.save(venueEntityTobeSave);

            TransactionEntity transactionEntity = new TransactionEntity();
            transactionEntity.setBookedByEmail(bookedByEmail);
            transactionEntity.setBookedStatus(true);
            transactionEntity.setBookedAt(new java.util.Date());
            transactionEntity.setVenue(venueEntityTobeSave);

            transactionRepository.save(transactionEntity);

            bookingResponse.setTransactionId(transactionEntity.getId());
            bookingResponse.setBookedByEmail(transactionEntity.getBookedByEmail());
            bookingResponse.setBookedStatus(transactionEntity.getVenue().isAvailable());
            bookingResponse.setVenueRecordBooked(transactionEntity.getVenue().getRecordID());
            bookingResponse.setMessage("Venue booked successfully");

            return bookingResponse;
        } catch (Exception e) {
            bookingResponse.setTransactionId(null);
            bookingResponse.setBookedStatus(false);
            bookingResponse.setVenueRecordBooked(null);
            bookingResponse.setMessage("Error in booking : " + e.getMessage());

            return bookingResponse;
        }
    }

    @Transactional
    public CancelBookingResponse cancelBooking(Long transactionId, String email) {

        TransactionEntity transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException(
                        "No transaction found with ID: " + transactionId));

        if (!transaction.getBookedByEmail().equals(email)) {
            throw new RuntimeException("Unauthorized: Email does not match booking record");
        }

        transaction.setBookedStatus(false);

        // make venue available
        VenueEntity venue = transaction.getVenue();
        venue.setAvailable(true);

        transactionRepository.save(transaction);

        CancelBookingResponse response = new CancelBookingResponse();
        response.setStatus(true);
        response.setMessage("Booking cancelled successfully");
        return response;
    }

}
