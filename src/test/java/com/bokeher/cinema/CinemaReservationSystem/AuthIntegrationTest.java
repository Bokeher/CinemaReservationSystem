package com.bokeher.cinema.CinemaReservationSystem;

import com.bokeher.cinema.CinemaReservationSystem.auth.dto.LoginUserRequest;
import com.bokeher.cinema.CinemaReservationSystem.auth.dto.RegisterUserRequest;
import com.bokeher.cinema.CinemaReservationSystem.user.User;
import com.bokeher.cinema.CinemaReservationSystem.user.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static com.bokeher.cinema.CinemaReservationSystem.user.UserFixtures.*;
import static com.bokeher.cinema.CinemaReservationSystem.auth.AuthFixtures.*;

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
    void register_shouldCreateUserAndReturnToken() throws Exception {
        RegisterUserRequest request = registerRequest().build();

        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/auth/register",
                request,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode body = objectMapper.readTree(response.getBody());

        assertThat(body.get("token").asText()).isNotBlank();
        assertThat(body.get("user").get("username").asText()).isEqualTo(USERNAME);
        assertThat(body.get("user").get("email").asText()).isEqualTo(EMAIL);
        assertThat(body.get("user").get("role").asText()).isEqualTo("USER");
        assertThat(body.get("user").has("password")).isFalse();
    }

    @Test
    void login_shouldLoginUserAndReturnToken() throws Exception {
        User user = userWithoutId()
                .password(passwordEncoder.encode(PASSWORD))
                .build();

        userRepository.save(user);

        LoginUserRequest request = loginRequest().build();

        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/auth/login",
                request,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode body = objectMapper.readTree(response.getBody());

        assertThat(body.get("token").asText()).isNotBlank();
        assertThat(body.get("user").get("username").asText()).isEqualTo(USERNAME);
        assertThat(body.get("user").get("email").asText()).isEqualTo(EMAIL);
        assertThat(body.get("user").has("password")).isFalse();
    }
}
