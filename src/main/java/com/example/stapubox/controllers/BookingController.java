package com.example.stapubox.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.stapubox.middleware.Auth;
import com.example.stapubox.models.payloads.BookingPayload;
import com.example.stapubox.models.payloads.CancelPayload;
import com.example.stapubox.models.payloads.JwtClaims;
import com.example.stapubox.models.response.BookingResponse;
import com.example.stapubox.models.response.CancelBookingResponse;
import com.example.stapubox.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/booking/user")
public class BookingController {
    @Autowired
    private Auth auth;
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/book")
    public BookingResponse bookVenue(@RequestBody BookingPayload bookingPayload,
            @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        JwtClaims claims = auth.parseToken(token);
        String email = claims.getEmail();
        if (email == null || email.isEmpty()) {
            BookingResponse bookingResponse = new BookingResponse();
            bookingResponse.setTransactionId(null);
            bookingResponse.setBookedStatus(false);
            bookingResponse.setVenueRecordBooked(null);
            bookingResponse.setMessage("Invalid token: email not found");
            return bookingResponse;
        }
        return transactionService.bookVenue(bookingPayload.getRecordId(), email);
    }

    @PostMapping("/cancel")
    public CancelBookingResponse cancelBooking(@RequestBody CancelPayload cancelPayload,
            @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        JwtClaims claims = auth.parseToken(token);
        String email = claims.getEmail();
        return transactionService.cancelBooking(cancelPayload.getTransactionId(), email);
    }
}
