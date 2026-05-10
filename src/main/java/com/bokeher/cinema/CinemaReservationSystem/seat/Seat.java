package com.bokeher.cinema.CinemaReservationSystem.seat;

import com.bokeher.cinema.CinemaReservationSystem.room.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(
    name = "seats",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"room_id", "row", "number"})
    }
)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    private int row;

    @Column(nullable = false)
    private int number;

}
