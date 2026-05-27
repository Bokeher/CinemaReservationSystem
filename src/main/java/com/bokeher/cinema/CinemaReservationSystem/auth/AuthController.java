package com.bokeher.cinema.CinemaReservationSystem.auth;

import com.bokeher.cinema.CinemaReservationSystem.auth.dto.AuthResponse;
import com.bokeher.cinema.CinemaReservationSystem.auth.dto.LoginUserRequest;
import com.bokeher.cinema.CinemaReservationSystem.auth.dto.RegisterUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse createUser(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        return authService.register(registerUserRequest);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginUserRequest loginUserRequest) {
        return authService.login(loginUserRequest);
    }

}
