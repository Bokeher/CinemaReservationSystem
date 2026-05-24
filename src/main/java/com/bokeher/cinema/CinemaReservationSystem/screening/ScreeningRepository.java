package com.bokeher.cinema.CinemaReservationSystem.screening;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

interface ScreeningRepository extends JpaRepository<Screening, Long> {

    @Query("""
        SELECT COUNT(s) > 0
        FROM Screening s
        WHERE s.room.id = :roomId
            AND (:screeningId IS NULL OR s.id <> :screeningId)
            AND s.startTime < :endTime
            AND s.endTime > :startTime
    """)
    boolean existsOverlappingScreening(Long roomId, Long screeningId, LocalDateTime startTime, LocalDateTime endTime);

    boolean existsByIdAndRoomSeatsId(Long screeningId, Long seatId);
}
