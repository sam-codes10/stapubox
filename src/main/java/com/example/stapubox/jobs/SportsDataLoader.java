package com.example.stapubox.jobs;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.stapubox.entities.SportEntity;
import com.example.stapubox.models.response.SportData;
import com.example.stapubox.models.response.SportsApiResponse;
import com.example.stapubox.repositoryDao.SportRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SportsDataLoader {

    private static final String SPORTS_API = "https://stapubox.com/sportslist/";

    private final SportRepository sportRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SportsDataLoader(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    @Transactional
    public void loadSportsData() throws Exception {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SPORTS_API))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to call sports API. Status: " + response.statusCode());
        }

        SportsApiResponse apiResponse = objectMapper.readValue(response.body(), SportsApiResponse.class);

        if (apiResponse == null ||
                apiResponse.getData() == null ||
                !"success".equals(apiResponse.getStatus())) {
            return;
        }
        for (SportData apiSport : apiResponse.getData()) {

            if (sportRepository.existsBySportId(apiSport.getSport_id())) {
                continue;
            }

            SportEntity sport = new SportEntity();
            sport.setSportId(apiSport.getSport_id());
            sport.setSportCode(apiSport.getSport_code());
            sport.setSportName(apiSport.getSport_name());

            sportRepository.save(sport);
        }

        System.out.println("Sports master data loaded successfully");
    }
}
