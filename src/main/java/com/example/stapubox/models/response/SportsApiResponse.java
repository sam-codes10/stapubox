package com.example.stapubox.models.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SportsApiResponse {
    private String status;
    private String msg;
    private String err;
    private List<SportData> data;
}
