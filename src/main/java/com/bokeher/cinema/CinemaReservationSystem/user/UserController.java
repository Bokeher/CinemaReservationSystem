package com.bokeher.cinema.CinemaReservationSystem.user;

import com.bokeher.cinema.CinemaReservationSystem.user.dto.LoginUserRequest;
import com.bokeher.cinema.CinemaReservationSystem.user.dto.RegisterUserRequest;
import com.bokeher.cinema.CinemaReservationSystem.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponse createUser(@RequestBody RegisterUserRequest registerUserRequest) {
        return userService.register(registerUserRequest);
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody LoginUserRequest loginUserRequest) {
        return userService.login(loginUserRequest);
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable long id) {
        return userService.findById(id);
    }

    @GetMapping
    public UserResponse getByUsername(@RequestParam String username) {
        return userService.findByUsername(username);
    }
}
