package com.bokeher.cinema.CinemaReservationSystem.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;

@Converter()
public class DurationToMinutesConverter implements AttributeConverter<Duration, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Duration duration) {
        return duration == null ? null : (int) duration.toMinutes();
    }

    @Override
    public Duration convertToEntityAttribute(Integer minutes) {
        return minutes == null ? null : Duration.ofMinutes(minutes);
    }
}
