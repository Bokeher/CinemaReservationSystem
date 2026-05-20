package com.bokeher.cinema.CinemaReservationSystem.screening;

import com.bokeher.cinema.CinemaReservationSystem.screening.dto.BriefScreeningResponse;
import com.bokeher.cinema.CinemaReservationSystem.screening.dto.DetailedScreeningResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/screenings")
@RequiredArgsConstructor
public class ScreeningController {

    private final ScreeningService screeningService;

    @GetMapping("/{id}")
    public DetailedScreeningResponse getScreening(@PathVariable Long id){
        return screeningService.findById(id);
    }

    @GetMapping
    public List<BriefScreeningResponse> getScreenings() {
        return screeningService.findAll();
    }
}
