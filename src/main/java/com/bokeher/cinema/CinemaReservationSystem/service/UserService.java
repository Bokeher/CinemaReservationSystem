package com.bokeher.cinema.CinemaReservationSystem.service;

import com.bokeher.cinema.CinemaReservationSystem.entity.User;
import com.bokeher.cinema.CinemaReservationSystem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
