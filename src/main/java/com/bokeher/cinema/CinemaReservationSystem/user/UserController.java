package com.bokeher.cinema.CinemaReservationSystem.user;

import com.bokeher.cinema.CinemaReservationSystem.security.UserPrincipal;
import com.bokeher.cinema.CinemaReservationSystem.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/me")
public class UserController {

    private final UserService userService;

    @GetMapping
    public UserResponse getMe(@AuthenticationPrincipal UserPrincipal user) {
        return userService.findByUsername(user.getUsername());
    }
}
