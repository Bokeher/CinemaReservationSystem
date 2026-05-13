package com.bokeher.cinema.CinemaReservationSystem.auth;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    public final UserPrincipalService userPrincipalService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {

            String username = jwtService.extractUsername(token);

            if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
                return;
            }

            UserDetails userDetails = userPrincipalService.loadUserByUsername(username);

            if (!jwtService.isTokenValid(token, userDetails)) {
                filterChain.doFilter(request, response);
                return;
            }

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);

        } catch (JwtException | IllegalArgumentException e) {
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
        }
    }

}
