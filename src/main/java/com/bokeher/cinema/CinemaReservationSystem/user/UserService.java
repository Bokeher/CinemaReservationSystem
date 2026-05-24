package com.bokeher.cinema.CinemaReservationSystem.user;

import com.bokeher.cinema.CinemaReservationSystem.user.dto.CreateUserRequest;
import com.bokeher.cinema.CinemaReservationSystem.user.dto.UpdateUserRequest;
import com.bokeher.cinema.CinemaReservationSystem.user.exception.EmailAlreadyExistsException;
import com.bokeher.cinema.CinemaReservationSystem.user.exception.UsernameAlreadyExistsException;
import com.bokeher.cinema.CinemaReservationSystem.user.dto.UserResponse;
import com.bokeher.cinema.CinemaReservationSystem.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
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

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.delete(user);
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        User savedUser = createUserInternal(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getRole()
        );

        return userMapper.toResponse(savedUser);
    }

    @Transactional
    public User createUserInternal(String username, String email, String rawPassword, UserRole role) {
        validateCreate(username, email);

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .role(role)
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        validatePatch(user, request);

        applyPatch(user, request);

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    private void applyPatch(User user, UpdateUserRequest request) {
        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
    }

    private void validatePatch(User user, UpdateUserRequest request) {
        if (request.getUsername() != null
                && !request.getUsername().equals(user.getUsername())
                && userRepository.existsByUsername(request.getUsername())) {

            throw new UsernameAlreadyExistsException(request.getUsername());
        }

        if (request.getEmail() != null
                && !request.getEmail().equals(user.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {

            throw new EmailAlreadyExistsException(request.getEmail());
        }
    }

    private void validateCreate(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(username);
        }

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
    }
}
