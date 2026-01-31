package org.pileka.fitness_tracker_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.pileka.fitness_tracker_api.domain.User;
import org.pileka.fitness_tracker_api.dto.auth.LoginDto;
import org.pileka.fitness_tracker_api.dto.auth.TokenDto;
import org.pileka.fitness_tracker_api.dto.auth.RegistrationDto;
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
    public void register(RegistrationDto request) {
        // Mapping manually here because the passwords aren't encoded in the DTO,
        // and if you have to encrypt and inject it manually why even bother?
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
    }

    @Override
    public TokenDto login(LoginDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // If we get here, credentials are valid
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        return getTokenDto(user.getUsername());
    }

    @Override
    public TokenDto refresh(String refreshToken) {
        if (jwtTokenProvider.tokenIsValid(refreshToken)) {
            String username = jwtTokenProvider.getUsernameFromToken(refreshToken);

            return getTokenDto(username);
        }
        else {
            // TODO probably throw an exception if a token is invalid
            return null;
        }
    }

    private TokenDto getTokenDto(String username) {
        return new TokenDto(jwtTokenProvider.generateBearerToken(username),
                jwtTokenProvider.getBearerTokenExpiration(),
                jwtTokenProvider.generateRefreshToken(username),
                jwtTokenProvider.getRefreshTokenExpiration()
        );
    }
}
