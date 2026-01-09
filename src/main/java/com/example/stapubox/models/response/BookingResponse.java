package com.example.stapubox.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long transactionId;
    private String bookedByEmail;
    private boolean bookedStatus;
    private Long venueRecordBooked;
    private String message;
}
