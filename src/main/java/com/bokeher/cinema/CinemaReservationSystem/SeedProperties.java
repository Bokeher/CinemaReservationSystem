package com.bokeher.cinema.CinemaReservationSystem;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "app.seed")
public class SeedProperties {

    private boolean demoMode;
    private boolean addAdmin;

}