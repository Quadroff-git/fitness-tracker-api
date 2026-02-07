package org.pileka.fitness_tracker_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.pileka.fitness_tracker_api.domain.User;
import org.pileka.fitness_tracker_api.dto.auth.LoginRequestDto;
import org.pileka.fitness_tracker_api.dto.auth.TokenDto;
import org.pileka.fitness_tracker_api.dto.auth.RegistrationDto;
import org.pileka.fitness_tracker_api.exception.EntityRestrictionViolationException;
import org.pileka.fitness_tracker_api.exception.RefreshTokenInvalidException;
import org.pileka.fitness_tracker_api.exception.UserLoginFailedException;
import org.pileka.fitness_tracker_api.mapper.UserMapper;
import org.pileka.fitness_tracker_api.repository.UserRepository;
import org.pileka.fitness_tracker_api.security.JwtTokenProvider;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements org.pileka.fitness_tracker_api.service.AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Override
    public void register(RegistrationDto request) throws EntityRestrictionViolationException {
        User newUser = userMapper.toModel(request);
        try {
            userRepository.save(newUser);
        } catch (DataIntegrityViolationException e) {
            throw new EntityRestrictionViolationException(e);
        }
    }

    @Override
    public TokenDto login(LoginRequestDto request) throws UserLoginFailedException  {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new UserLoginFailedException(e);
        }

        return getTokenDto(request.getUsername());
    }

    @Override
    public TokenDto refresh(String refreshToken) throws RefreshTokenInvalidException {
        if (jwtTokenProvider.tokenIsValid(refreshToken)) {
            String username = jwtTokenProvider.getUsernameFromToken(refreshToken);

            return getTokenDto(username);
        }
        else {
            throw new RefreshTokenInvalidException("Token refreshing with an invalid refresh token was attempted");
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
