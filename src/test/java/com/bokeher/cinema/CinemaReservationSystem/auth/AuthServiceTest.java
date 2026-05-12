package com.bokeher.cinema.CinemaReservationSystem.auth;

import com.bokeher.cinema.CinemaReservationSystem.auth.dto.RegisterUserRequest;
import com.bokeher.cinema.CinemaReservationSystem.auth.exception.EmailAlreadyExistsException;
import com.bokeher.cinema.CinemaReservationSystem.auth.exception.UsernameAlreadyExistsException;
import com.bokeher.cinema.CinemaReservationSystem.user.User;
import com.bokeher.cinema.CinemaReservationSystem.user.UserMapper;
import com.bokeher.cinema.CinemaReservationSystem.user.UserRepository;
import com.bokeher.cinema.CinemaReservationSystem.user.UserRole;
import com.bokeher.cinema.CinemaReservationSystem.user.dto.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    private static final String USERNAME = "john";
    private static final String EMAIL = "john@example.com";
    private static final String PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encoded";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, passwordEncoder, new AuthMapper(), new UserMapper());
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterUserRequest request = getRequest();
        User savedUser = getSavedUser();

        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse result = authService.register(request);

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
                () -> authService.register(request)
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
                () -> authService.register(request)
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

        authService.register(request);

        verify(passwordEncoder).encode(PASSWORD);
        verify(userRepository).save(argThat(user ->
                ENCODED_PASSWORD.equals(user.getPassword())
        ));
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
