package com.bokeher.cinema.CinemaReservationSystem.entity;

import com.bokeher.cinema.CinemaReservationSystem.converter.DurationToMinutesConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private int requiredAge;

    @Convert(converter = DurationToMinutesConverter.class)
    @Column(name = "duration_minutes")
    private Duration duration;
}
