package org.pileka.fitness_tracker_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.pileka.fitness_tracker_api.domain.User;
import org.pileka.fitness_tracker_api.dto.auth.LoginRequest;
import org.pileka.fitness_tracker_api.dto.auth.TokenDto;
import org.pileka.fitness_tracker_api.dto.auth.RegistrationRequest;
import org.pileka.fitness_tracker_api.repository.UserRepository;
import org.pileka.fitness_tracker_api.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements org.pileka.fitness_tracker_api.service.AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    public boolean register(RegistrationRequest request) {
        // Create new user with encoded password
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
        return true;
    }

    @Override
    public TokenDto login(LoginRequest request) {
        // Let Spring Security validate credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // If we get here, credentials are valid
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        // Generate and return JWT
        String bearerToken = jwtTokenProvider.generateBearerToken(user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        return new TokenDto(bearerToken, refreshToken);
    }
}
