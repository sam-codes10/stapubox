package com.example.stapubox.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelBookingResponse {
    private boolean status;
    private String message;
}
