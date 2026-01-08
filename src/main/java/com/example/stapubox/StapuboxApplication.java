package com.example.stapubox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.stapubox.jobs.SportsDataLoader;

@SpringBootApplication
public class StapuboxApplication {

	private SportsDataLoader sportsDataLoader;

	StapuboxApplication(SportsDataLoader sportsDataLoader) {
		this.sportsDataLoader = sportsDataLoader;
		try {
			sportsDataLoader.loadSportsData();
		} catch (Exception e) {
			System.err.println("Failed to load sports data: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(StapuboxApplication.class, args);
	}

}
