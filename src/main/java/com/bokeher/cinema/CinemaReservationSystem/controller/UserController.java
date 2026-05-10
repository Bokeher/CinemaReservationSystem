package com.bokeher.cinema.CinemaReservationSystem.controller;

import com.bokeher.cinema.CinemaReservationSystem.entity.User;
import com.bokeher.cinema.CinemaReservationSystem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.register(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<User> getByUsername(@RequestParam String username) {
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
