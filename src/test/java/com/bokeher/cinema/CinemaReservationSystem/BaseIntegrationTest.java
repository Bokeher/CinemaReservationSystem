package com.bokeher.cinema.CinemaReservationSystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(BaseIntegrationTest.TestcontainersConfiguration.class)
abstract class BaseIntegrationTest {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Autowired
    protected ObjectMapper objectMapper;

    @TestConfiguration(proxyBeanMethods = false)
    static class TestcontainersConfiguration {

        @Bean
        @ServiceConnection
        MySQLContainer<?> mysqlContainer() {
            //noinspection resource
            return new MySQLContainer<>("mysql:8.4.6")
                    .withDatabaseName("cinema_test")
                    .withUsername("test")
                    .withPassword("test");
        }
    }
}
