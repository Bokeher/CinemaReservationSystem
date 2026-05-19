package com.bokeher.cinema.CinemaReservationSystem.screening;

import com.bokeher.cinema.CinemaReservationSystem.screening.dto.CreateScreeningRequest;
import com.bokeher.cinema.CinemaReservationSystem.screening.dto.DetailedScreeningResponse;
import com.bokeher.cinema.CinemaReservationSystem.screening.dto.UpdateScreeningRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/screenings")
@RequiredArgsConstructor
public class AdminScreeningController {

    private final ScreeningService screeningService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public DetailedScreeningResponse createScreening(@Valid @RequestBody CreateScreeningRequest request) {
        return screeningService.createScreening(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteScreening(@PathVariable Long id) {
        screeningService.deleteScreening(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public DetailedScreeningResponse updateScreening(@PathVariable Long id, @Valid @RequestBody UpdateScreeningRequest request) {
        return screeningService.updateScreening(id, request);
    }
}
