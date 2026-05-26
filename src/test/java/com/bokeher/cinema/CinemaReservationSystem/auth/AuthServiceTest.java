package com.bokeher.cinema.CinemaReservationSystem.auth;

import com.bokeher.cinema.CinemaReservationSystem.auth.dto.AuthResponse;
import com.bokeher.cinema.CinemaReservationSystem.auth.dto.LoginUserRequest;
import com.bokeher.cinema.CinemaReservationSystem.auth.dto.RegisterUserRequest;
import com.bokeher.cinema.CinemaReservationSystem.auth.exception.InvalidCredentialsException;
import com.bokeher.cinema.CinemaReservationSystem.security.JwtService;
import com.bokeher.cinema.CinemaReservationSystem.security.UserPrincipal;
import com.bokeher.cinema.CinemaReservationSystem.user.User;
import com.bokeher.cinema.CinemaReservationSystem.user.UserMapper;
import com.bokeher.cinema.CinemaReservationSystem.user.UserRepository;
import com.bokeher.cinema.CinemaReservationSystem.user.UserRole;
import com.bokeher.cinema.CinemaReservationSystem.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.bokeher.cinema.CinemaReservationSystem.auth.AuthAssertions.assertAuthResponse;
import static com.bokeher.cinema.CinemaReservationSystem.auth.AuthFixtures.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static com.bokeher.cinema.CinemaReservationSystem.user.UserFixtures.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(
                userRepository,
                passwordEncoder,
                new UserMapper(),
                jwtService,
                userService
        );
    }

    @Test
    void shouldLoginSuccessfully() {
        LoginUserRequest request = loginRequest().build();
        User user = userWithId().build();

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
        when(jwtService.generateToken(any(UserPrincipal.class))).thenReturn(TOKEN);

        AuthResponse response = authService.login(request);

        assertAuthResponse(TOKEN, user, response);
    }

    @Test
    void shouldThrowWhenUserDoesNotExistDuringLogin() {
        LoginUserRequest request = loginRequest().build();

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        verifyNoInteractions(passwordEncoder, jwtService, userService);
    }

    @Test
    void shouldThrowWhenPasswordDoesNotMatchDuringLogin() {
        LoginUserRequest request = loginRequest().build();
        User user = userWithId().build();

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(false);

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterUserRequest request = registerRequest().build();
        User savedUser = userWithId().build();

        when(userService.createUserInternal(USERNAME, EMAIL, PASSWORD, UserRole.USER)).thenReturn(savedUser);
        when(jwtService.generateToken(any(UserPrincipal.class))).thenReturn(TOKEN);

        AuthResponse result = authService.register(request);

        assertAuthResponse(TOKEN, savedUser, result);
    }

}
