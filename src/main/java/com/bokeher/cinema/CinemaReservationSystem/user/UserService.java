package com.bokeher.cinema.CinemaReservationSystem.user;

import com.bokeher.cinema.CinemaReservationSystem.user.dto.LoginUserRequest;
import com.bokeher.cinema.CinemaReservationSystem.user.dto.RegisterUserRequest;
import com.bokeher.cinema.CinemaReservationSystem.user.dto.UserResponse;
import com.bokeher.cinema.CinemaReservationSystem.user.exception.EmailAlreadyExistsException;
import com.bokeher.cinema.CinemaReservationSystem.user.exception.InvalidCredentialsException;
import com.bokeher.cinema.CinemaReservationSystem.user.exception.UserNotFoundException;
import com.bokeher.cinema.CinemaReservationSystem.user.exception.UsernameAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserResponse register(RegisterUserRequest request) {
        User user = userMapper.toEntity(request);

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException(user.getUsername());
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException(user.getEmail());
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);

        user.setRole(UserRole.USER);

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    public UserResponse login(LoginUserRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return userMapper.toResponse(user);
    }

    public UserResponse findById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return userMapper.toResponse(user);
    }

    public UserResponse findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return userMapper.toResponse(user);
    }

}
