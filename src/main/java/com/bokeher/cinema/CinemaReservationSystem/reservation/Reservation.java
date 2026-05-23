package com.bokeher.cinema.CinemaReservationSystem.reservation;

import com.bokeher.cinema.CinemaReservationSystem.screening.Screening;
import com.bokeher.cinema.CinemaReservationSystem.seat.Seat;
import com.bokeher.cinema.CinemaReservationSystem.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screening_id", nullable = false)
    private Screening screening;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
}
