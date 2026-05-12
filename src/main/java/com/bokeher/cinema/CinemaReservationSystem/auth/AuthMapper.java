package com.bokeher.cinema.CinemaReservationSystem.auth;

import com.bokeher.cinema.CinemaReservationSystem.auth.dto.RegisterUserRequest;
import com.bokeher.cinema.CinemaReservationSystem.user.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public User toEntity(RegisterUserRequest registerUserRequest) {
        User user = new User();
        user.setUsername(registerUserRequest.getUsername());
        user.setPassword(registerUserRequest.getPassword());
        user.setEmail(registerUserRequest.getEmail());
        return user;
    }

}
