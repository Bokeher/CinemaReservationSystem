package com.bokeher.cinema.CinemaReservationSystem;

import com.bokeher.cinema.CinemaReservationSystem.user.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AuthIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanUsers() {
        userRepository.deleteAll();
    }

    @Test
    void registerShouldCreateUserAndReturnToken() throws Exception {
        final String USERNAME = "adam";
        final String EMAIL = "adam@example.com";
        final String PASSWORD = "password123";

        var request = Map.of(
                "username", USERNAME,
                "email", EMAIL,
                "password", PASSWORD
        );

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
}
