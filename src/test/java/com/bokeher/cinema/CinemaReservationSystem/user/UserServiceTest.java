package com.bokeher.cinema.CinemaReservationSystem.user;

import com.bokeher.cinema.CinemaReservationSystem.user.dto.UserResponse;
import com.bokeher.cinema.CinemaReservationSystem.user.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final String USERNAME = "john";
    private static final String EMAIL = "john@example.com";

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, new UserMapper());
    }

    @Test
    void shouldFindUserById() {
        User user = getSavedUser();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse result = userService.findById(1L);

        assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals(USERNAME, result.getUsername()),
                () -> assertEquals(EMAIL, result.getEmail()),
                () -> assertEquals(UserRole.USER, result.getRole())
        );

        verify(userRepository).findById(1L);
    }

    @Test
    void shouldThrowWhenUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.findById(1L)
        );

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository).findById(1L);
    }

    @Test
    void shouldFindUserByUsername() {
        User user = getSavedUser();

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        UserResponse result = userService.findByUsername(USERNAME);

        assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals(USERNAME, result.getUsername()),
                () -> assertEquals(EMAIL, result.getEmail()),
                () -> assertEquals(UserRole.USER, result.getRole())
        );

        verify(userRepository).findByUsername(USERNAME);
    }

    @Test
    void shouldThrowWhenUserByUsernameNotFound() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.findByUsername(USERNAME)
        );

        assertEquals("User not found with name: " + USERNAME, exception.getMessage());
        verify(userRepository).findByUsername(USERNAME);
    }
    private User getSavedUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername(USERNAME);
        user.setEmail(EMAIL);
        user.setPassword("encoded");
        user.setRole(UserRole.USER);
        return user;
    }
}
