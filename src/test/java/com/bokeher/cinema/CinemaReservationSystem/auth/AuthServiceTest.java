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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    private static final String USERNAME = "john";
    private static final String EMAIL = "john@example.com";
    private static final String PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encoded";
    private static final String TOKEN = "token";

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
        LoginUserRequest request = getLoginRequest();
        User user = getSavedUser();

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
        when(jwtService.generateToken(any(UserPrincipal.class))).thenReturn(TOKEN);

        AuthResponse response = authService.login(request);

        assertAll(
                () -> assertEquals(USERNAME, response.getUser().getUsername()),
                () -> assertEquals(EMAIL, response.getUser().getEmail()),
                () -> assertEquals(UserRole.USER, response.getUser().getRole()),
                () -> assertEquals(TOKEN, response.getToken())
        );

        verify(userRepository).findByUsername(USERNAME);
        verify(passwordEncoder).matches(PASSWORD, ENCODED_PASSWORD);
        verify(jwtService).generateToken(any(UserPrincipal.class));

    }

    @Test
    void shouldThrowWhenUserDoesNotExistDuringLogin() {
        LoginUserRequest request = getLoginRequest();

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        assertEquals("Invalid username or password", exception.getMessage());
        verify(userRepository).findByUsername(USERNAME);
        verifyNoInteractions(passwordEncoder, jwtService, userService);
    }

    @Test
    void shouldThrowWhenPasswordDoesNotMatchDuringLogin() {
        LoginUserRequest request = getLoginRequest();
        User user = getSavedUser();

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(false);

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        assertEquals("Invalid username or password", exception.getMessage());
        verify(userRepository).findByUsername(USERNAME);
        verify(passwordEncoder).matches(PASSWORD, ENCODED_PASSWORD);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterUserRequest request = getRegisterRequest();
        User savedUser = getSavedUser();

        when(userService.createUserInternal(USERNAME, EMAIL, PASSWORD, UserRole.USER)).thenReturn(savedUser);
        when(jwtService.generateToken(any(UserPrincipal.class))).thenReturn(TOKEN);

        AuthResponse result = authService.register(request);

        assertAll(
                () -> assertEquals(USERNAME, result.getUser().getUsername()),
                () -> assertEquals(EMAIL, result.getUser().getEmail()),
                () -> assertEquals(UserRole.USER, result.getUser().getRole()),
                () -> assertEquals(TOKEN, result.getToken())
        );

        verify(userService).createUserInternal(USERNAME, EMAIL, PASSWORD, UserRole.USER);
        verify(jwtService).generateToken(any(UserPrincipal.class));
    }

    private RegisterUserRequest getRegisterRequest() {
        return RegisterUserRequest.builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();
    }

    private LoginUserRequest getLoginRequest() {
        return LoginUserRequest.builder()
                .username(USERNAME)
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
