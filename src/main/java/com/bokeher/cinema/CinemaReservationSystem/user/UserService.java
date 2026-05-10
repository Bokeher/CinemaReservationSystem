package com.bokeher.cinema.CinemaReservationSystem.user;

import com.bokeher.cinema.CinemaReservationSystem.user.dto.RegisterUserRequest;
import com.bokeher.cinema.CinemaReservationSystem.user.dto.UserResponse;
import com.bokeher.cinema.CinemaReservationSystem.user.exception.UserNotFoundException;
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

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);

        user.setRole(UserRole.USER);

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
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
