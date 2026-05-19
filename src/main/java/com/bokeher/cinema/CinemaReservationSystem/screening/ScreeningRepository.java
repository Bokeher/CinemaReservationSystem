package com.bokeher.cinema.CinemaReservationSystem.screening;

import org.springframework.data.jpa.repository.JpaRepository;

interface ScreeningRepository extends JpaRepository<Screening, Long> {
}
