package com.bokeher.cinema.CinemaReservationSystem.user.dto;

import com.bokeher.cinema.CinemaReservationSystem.user.UserRole;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    private String username;
    private String email;
    private String password;
    private UserRole role;
}
