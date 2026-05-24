package com.bokeher.cinema.CinemaReservationSystem.user;

import com.bokeher.cinema.CinemaReservationSystem.user.dto.UserResponse;
import com.bokeher.cinema.CinemaReservationSystem.user.exception.EmailAlreadyExistsException;
import com.bokeher.cinema.CinemaReservationSystem.user.exception.UserNotFoundException;
import com.bokeher.cinema.CinemaReservationSystem.user.exception.UsernameAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.bokeher.cinema.CinemaReservationSystem.user.UserAssertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.bokeher.cinema.CinemaReservationSystem.user.UserFixtures.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, new UserMapper(), passwordEncoder);
    }

    @Test
    void shouldFindUserById() {
        User user = anyUser().build();
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        UserResponse result = userService.findById(USER_ID);

        assertUserResponse(user, result);
    }

    @Test
    void shouldThrowWhenUserByIdNotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.findById(USER_ID)
        );
    }

    @Test
    void shouldFindUserByUsername() {
        User user = anyUser().build();
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        UserResponse result = userService.findByUsername(USERNAME);

        assertUserResponse(user, result);
    }

    @Test
    void shouldThrowWhenUserByUsernameNotFound() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.findByUsername(USERNAME)
        );
    }

    @Test
    void shouldDeleteUser() {
        User user = anyUser().build();
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        userService.deleteUser(USER_ID);

        verify(userRepository).delete(user);
    }

    @Test
    void shouldThrowWhenDeletingMissingUser() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteUser(USER_ID)
        );
    }

    @Test
    void shouldCreateUserInternal() {
        User user = anyUser().build();

        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createUserInternal(USERNAME, EMAIL, PASSWORD, UserRole.USER);

        assertUser(user, result);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();
        assertUserCaptured(user, saved);
    }

    @Test
    void shouldThrowWhenUsernameAlreadyExistsDuringCreateUserInternal() {
        when(userRepository.existsByUsername(USERNAME)).thenReturn(true);

        assertThrows(
                UsernameAlreadyExistsException.class,
                () -> userService.createUserInternal(USERNAME, EMAIL, PASSWORD, UserRole.USER)
        );
    }

    @Test
    void shouldThrowWhenEmailAlreadyExistsDuringCreateUserInternal() {
        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(EMAIL)).thenReturn(true);

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> userService.createUserInternal(USERNAME, EMAIL, PASSWORD, UserRole.USER)
        );

    }

}
