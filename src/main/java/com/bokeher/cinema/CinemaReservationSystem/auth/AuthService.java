package com.bokeher.cinema.CinemaReservationSystem.auth;

import com.bokeher.cinema.CinemaReservationSystem.auth.dto.AuthResponse;
import com.bokeher.cinema.CinemaReservationSystem.security.JwtService;
import com.bokeher.cinema.CinemaReservationSystem.security.UserPrincipal;
import com.bokeher.cinema.CinemaReservationSystem.user.*;
import com.bokeher.cinema.CinemaReservationSystem.auth.dto.LoginUserRequest;
import com.bokeher.cinema.CinemaReservationSystem.auth.dto.RegisterUserRequest;
import com.bokeher.cinema.CinemaReservationSystem.auth.exception.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final UserService userService;

    @Transactional
    public AuthResponse register(RegisterUserRequest request) {

        User saved = userService.createUserInternal(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                UserRole.USER
        );

        String token = jwtService.generateToken(new UserPrincipal(saved));

        return new AuthResponse(token, userMapper.toResponse(saved));
    }

    public AuthResponse login(LoginUserRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(new UserPrincipal(user));

        return new AuthResponse(token, userMapper.toResponse(user));
    }
}
