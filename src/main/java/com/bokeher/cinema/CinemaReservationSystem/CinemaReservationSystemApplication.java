package com.bokeher.cinema.CinemaReservationSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(SeedProperties.class)
@SpringBootApplication
public class CinemaReservationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinemaReservationSystemApplication.class, args);
	}

}
