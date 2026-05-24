package com.bokeher.cinema.CinemaReservationSystem.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByUserId(Long userId);

    boolean existsByScreeningIdAndSeatIdAndStatusIn(
            Long screeningId,
            Long seatId,
            List<ReservationStatus> statuses
    );

}
