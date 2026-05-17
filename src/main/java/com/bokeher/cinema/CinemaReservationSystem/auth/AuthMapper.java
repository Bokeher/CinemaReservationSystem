package com.bokeher.cinema.CinemaReservationSystem.auth;

import com.bokeher.cinema.CinemaReservationSystem.auth.dto.RegisterUserRequest;
import com.bokeher.cinema.CinemaReservationSystem.user.User;
import com.bokeher.cinema.CinemaReservationSystem.user.UserRole;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public User toEntity(RegisterUserRequest request) {
        return User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .role(UserRole.USER)
                .build();
    }

}
