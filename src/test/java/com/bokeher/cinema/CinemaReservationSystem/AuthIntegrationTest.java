package com.bokeher.cinema.CinemaReservationSystem;

import com.bokeher.cinema.CinemaReservationSystem.auth.dto.AuthResponse;
import com.bokeher.cinema.CinemaReservationSystem.auth.dto.LoginUserRequest;
import com.bokeher.cinema.CinemaReservationSystem.auth.dto.RegisterUserRequest;
import com.bokeher.cinema.CinemaReservationSystem.user.User;
import com.bokeher.cinema.CinemaReservationSystem.user.UserRepository;
import com.bokeher.cinema.CinemaReservationSystem.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.bokeher.cinema.CinemaReservationSystem.auth.AuthFixtures.loginRequest;
import static com.bokeher.cinema.CinemaReservationSystem.auth.AuthFixtures.registerRequest;
import static com.bokeher.cinema.CinemaReservationSystem.user.UserFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

class AuthIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanUsers() {
        userRepository.deleteAll();
    }

    @Test
    void register_shouldCreateUserAndReturnToken() {
        RegisterUserRequest request = registerRequest().build();

        ResponseEntity<AuthResponse> response = testRestTemplate.postForEntity(
                "/auth/register",
                request,
                AuthResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        AuthResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.getToken()).isNotBlank();

        assertThat(body.getUser()).isNotNull();
        assertThat(body.getUser().getUsername()).isEqualTo(USERNAME);
        assertThat(body.getUser().getEmail()).isEqualTo(EMAIL);
        assertThat(body.getUser().getRole()).isEqualTo(UserRole.USER);
    }

    @Test
    void login_shouldLoginUserAndReturnToken() {
        User user = userWithoutId()
                .password(passwordEncoder.encode(PASSWORD))
                .build();

        userRepository.save(user);

        LoginUserRequest request = loginRequest().build();

        ResponseEntity<AuthResponse> response = testRestTemplate.postForEntity(
                "/auth/login",
                request,
                AuthResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        AuthResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.getToken()).isNotBlank();

        assertThat(body.getUser()).isNotNull();
        assertThat(body.getUser().getUsername()).isEqualTo(USERNAME);
        assertThat(body.getUser().getEmail()).isEqualTo(EMAIL);
    }

    @Test
    void login_shouldThrowException_whenProvidedWrongPassword() {
        User user = userWithoutId()
                .password(passwordEncoder.encode("CORRECT_PASSWORD"))
                .build();

        userRepository.save(user);

        LoginUserRequest request = loginRequest()
                .password("WRONG_PASSWORD")
                .build();

        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/auth/login",
                request,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(response.getBody())
                .isEqualTo("Invalid username or password");
    }

    @Test
    void login_shouldThrowException_whenNoSuchUserInDb() {
        LoginUserRequest request = loginRequest().build();

        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/auth/login",
                request,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(response.getBody())
                .isEqualTo("Invalid username or password");
    }
}