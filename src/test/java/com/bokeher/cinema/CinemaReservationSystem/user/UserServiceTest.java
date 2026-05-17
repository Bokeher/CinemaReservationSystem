package com.bokeher.cinema.CinemaReservationSystem.user;

import com.bokeher.cinema.CinemaReservationSystem.user.dto.UserResponse;
import com.bokeher.cinema.CinemaReservationSystem.user.exception.EmailAlreadyExistsException;
import com.bokeher.cinema.CinemaReservationSystem.user.exception.UserNotFoundException;
import com.bokeher.cinema.CinemaReservationSystem.user.exception.UsernameAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final Long USER_ID = 1L;
    private static final String USERNAME = "john";
    private static final String EMAIL = "john@example.com";
    private static final String PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encoded";

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
        User user = getSavedUser();

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        UserResponse result = userService.findById(USER_ID);

        assertAll(
                () -> assertEquals(USER_ID, result.getId()),
                () -> assertEquals(USERNAME, result.getUsername()),
                () -> assertEquals(EMAIL, result.getEmail()),
                () -> assertEquals(UserRole.USER, result.getRole())
        );

        verify(userRepository).findById(USER_ID);
    }

    @Test
    void shouldThrowWhenUserByIdNotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.findById(USER_ID)
        );

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository).findById(USER_ID);
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

    @Test
    void shouldDeleteUser() {
        User user = getSavedUser();

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        userService.deleteUser(USER_ID);

        verify(userRepository).findById(USER_ID);
        verify(userRepository).delete(user);
    }

    @Test
    void shouldThrowWhenDeletingMissingUser() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteUser(USER_ID)
        );

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository).findById(USER_ID);
        verify(userRepository, never()).delete(any());
    }

    @Test
    void shouldCreateUserInternal() {
        User savedUser = getSavedUser();

        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.createUserInternal(USERNAME, EMAIL, PASSWORD, UserRole.ADMIN);

        assertAll(
                () -> assertEquals(USER_ID, result.getId()),
                () -> assertEquals(USERNAME, result.getUsername()),
                () -> assertEquals(EMAIL, result.getEmail()),
                () -> assertEquals(UserRole.USER, result.getRole())
        );
        verify(userRepository).existsByUsername(USERNAME);
        verify(userRepository).existsByEmail(EMAIL);
        verify(passwordEncoder).encode(PASSWORD);
        verify(userRepository).save(argThat(user ->
                USERNAME.equals(user.getUsername())
                        && EMAIL.equals(user.getEmail())
                        && ENCODED_PASSWORD.equals(user.getPassword())
                        && UserRole.ADMIN.equals(user.getRole())
        ));
    }

    @Test
    void shouldThrowWhenUsernameAlreadyExistsDuringCreateUserInternal() {
        when(userRepository.existsByUsername(USERNAME)).thenReturn(true);

        UsernameAlreadyExistsException exception = assertThrows(
                UsernameAlreadyExistsException.class,
                () -> userService.createUserInternal(USERNAME, EMAIL, PASSWORD, UserRole.USER)
        );

        assertEquals("Username already exists: " + USERNAME, exception.getMessage());
        verify(userRepository).existsByUsername(USERNAME);
        verify(userRepository, never()).existsByEmail(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void shouldThrowWhenEmailAlreadyExistsDuringCreateUserInternal() {
        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(EMAIL)).thenReturn(true);

        EmailAlreadyExistsException exception = assertThrows(
                EmailAlreadyExistsException.class,
                () -> userService.createUserInternal(USERNAME, EMAIL, PASSWORD, UserRole.USER)
        );

        assertEquals("Email already exists: " + EMAIL, exception.getMessage());
        verify(userRepository).existsByUsername(USERNAME);
        verify(userRepository).existsByEmail(EMAIL);
        verify(userRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }

    private User getSavedUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setUsername(USERNAME);
        user.setEmail(EMAIL);
        user.setPassword(ENCODED_PASSWORD);
        user.setRole(UserRole.USER);
        return user;
    }
}
