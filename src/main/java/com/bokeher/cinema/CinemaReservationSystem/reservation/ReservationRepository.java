package com.bokeher.cinema.CinemaReservationSystem.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByUserId(Long userId);

    boolean existsByScreeningIdAndSeatIdAndStatusIn(
            Long screeningId,
            Long seatId,
            List<ReservationStatus> statuses
    );

    @Query("""
        SELECT r.seat.id
        FROM Reservation r
        WHERE r.screening.id = :screeningId
        AND r.active = true
    """)
    Set<Long> findReservedSeatIds(Long screeningId);

}
