package com.bokeher.cinema.CinemaReservationSystem.user;

import com.bokeher.cinema.CinemaReservationSystem.user.dto.CreateUserRequest;
import com.bokeher.cinema.CinemaReservationSystem.user.exception.EmailAlreadyExistsException;
import com.bokeher.cinema.CinemaReservationSystem.user.exception.UsernameAlreadyExistsException;
import com.bokeher.cinema.CinemaReservationSystem.user.dto.UserResponse;
import com.bokeher.cinema.CinemaReservationSystem.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

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

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.delete(user);
    }

    public UserResponse createUser(CreateUserRequest request) {
        User savedUser = createUserInternal(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getRole()
        );

        return userMapper.toResponse(savedUser);
    }

    public User createUserInternal(String username, String email, String rawPassword, UserRole role) {

        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(username);
        }

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .role(role)
                .build();

        return userRepository.save(user);
    }



}
