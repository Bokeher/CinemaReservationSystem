package com.bokeher.cinema.CinemaReservationSystem.user;

import com.bokeher.cinema.CinemaReservationSystem.user.dto.RegisterUserRequest;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
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
        userService = new UserService(userRepository, passwordEncoder, new UserMapper());
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterUserRequest request = getRequest();
        User savedUser = getSavedUser();

        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse result = userService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        assertAll(
                () -> assertEquals(USERNAME, result.getUsername()),
                () -> assertEquals(EMAIL, result.getEmail()),
                () -> assertEquals(UserRole.USER, result.getRole())
        );

        verify(userRepository).save(userCaptor.capture());

        User persistedUser = userCaptor.getValue();
        assertAll(
                () -> assertEquals(USERNAME, persistedUser.getUsername()),
                () -> assertEquals(EMAIL, persistedUser.getEmail()),
                () -> assertEquals(ENCODED_PASSWORD, persistedUser.getPassword()),
                () -> assertEquals(UserRole.USER, persistedUser.getRole())
        );
    }

    @Test
    void shouldThrowWhenUsernameAlreadyExists() {
        RegisterUserRequest request = getRequest();

        when(userRepository.existsByUsername(USERNAME)).thenReturn(true);

        UsernameAlreadyExistsException exception = assertThrows(
                UsernameAlreadyExistsException.class,
                () -> userService.register(request)
        );

        assertEquals("Username already exists: " + USERNAME, exception.getMessage());
        verify(userRepository).existsByUsername(USERNAME);
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        RegisterUserRequest request = getRequest();

        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(EMAIL)).thenReturn(true);

        EmailAlreadyExistsException exception = assertThrows(
                EmailAlreadyExistsException.class,
                () -> userService.register(request)
        );

        assertEquals("Email already exists: " + EMAIL, exception.getMessage());
        verify(userRepository).existsByUsername(USERNAME);
        verify(userRepository).existsByEmail(EMAIL);
        verify(userRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void shouldEncodePasswordBeforeSavingUser() {
        RegisterUserRequest request = getRequest();

        User savedUser = getSavedUser();

        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        userService.register(request);

        verify(passwordEncoder).encode(PASSWORD);
        verify(userRepository).save(argThat(u ->
                ENCODED_PASSWORD.equals(u.getPassword())
        ));
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

    private RegisterUserRequest getRequest() {
        return RegisterUserRequest.builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();
    }

    private User getSavedUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername(USERNAME);
        user.setEmail(EMAIL);
        user.setPassword(ENCODED_PASSWORD);
        user.setRole(UserRole.USER);
        return user;
    }
}
