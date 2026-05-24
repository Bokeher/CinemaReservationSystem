package com.bokeher.cinema.CinemaReservationSystem.reservation;

import com.bokeher.cinema.CinemaReservationSystem.reservation.dto.ReservationResponse;
import com.bokeher.cinema.CinemaReservationSystem.seat.Seat;
import com.bokeher.cinema.CinemaReservationSystem.seat.dto.SeatResponse;

import static com.bokeher.cinema.CinemaReservationSystem.screening.ScreeningAssertions.assertBriefScreeningResponse;
import static com.bokeher.cinema.CinemaReservationSystem.screening.ScreeningAssertions.assertScreening;
import static com.bokeher.cinema.CinemaReservationSystem.user.UserAssertions.assertUser;
import static com.bokeher.cinema.CinemaReservationSystem.user.UserAssertions.assertUserResponse;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReservationAssertions {

    private ReservationAssertions() {}

    public static void assertReservationResponse(Reservation expected, ReservationResponse actual) {
        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertUserResponse(expected.getUser(), actual.getUser()),
                () -> assertBriefScreeningResponse(expected.getScreening(), actual.getScreening()),
                () -> assertSeatResponse(expected.getSeat(), actual.getSeat()),
                () -> assertEquals(expected.getStatus(), actual.getStatus())
        );
    }

    public static void assertReservation(Reservation expected, Reservation actual) {
        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertUser(expected.getUser(), actual.getUser()),
                () -> assertScreening(expected.getScreening(), actual.getScreening()),
                () -> assertSeat(expected.getSeat(), actual.getSeat()),
                () -> assertEquals(expected.getStatus(), actual.getStatus()),
                () -> assertEquals(expected.getActive(), actual.getActive())
        );
    }

    public static void assertCapturedReservation(Reservation expected, Reservation actual) {
        assertAll(
                () -> assertUser(expected.getUser(), actual.getUser()),
                () -> assertScreening(expected.getScreening(), actual.getScreening()),
                () -> assertSeat(expected.getSeat(), actual.getSeat()),
                () -> assertEquals(expected.getStatus(), actual.getStatus()),
                () -> assertEquals(expected.getActive(), actual.getActive())
        );
    }

    private static void assertSeatResponse(Seat expected, SeatResponse actual) {
        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getRow(), actual.getRow()),
                () -> assertEquals(expected.getNumber(), actual.getNumber())
        );
    }

    private static void assertSeat(Seat expected, Seat actual) {
        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getRow(), actual.getRow()),
                () -> assertEquals(expected.getNumber(), actual.getNumber())
        );
    }

}
