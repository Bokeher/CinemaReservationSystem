package com.bokeher.cinema.CinemaReservationSystem.movie;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    private String description;

    @Column(nullable = false)
    private int requiredAge;

    @Convert(converter = DurationToMinutesConverter.class)
    @Column(name = "duration_minutes", nullable = false)
    private Duration duration;
}
