package org.pileka.fitness_tracker_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pileka.fitness_tracker_api.domain.User;
import org.pileka.fitness_tracker_api.dto.auth.LoginRequestDto;
import org.pileka.fitness_tracker_api.dto.auth.RegistrationDto;
import org.pileka.fitness_tracker_api.dto.auth.TokenDto;
import org.pileka.fitness_tracker_api.exception.EntityRestrictionViolationException;
import org.pileka.fitness_tracker_api.exception.RefreshTokenInvalidException;
import org.pileka.fitness_tracker_api.exception.UserLoginFailedException;
import org.pileka.fitness_tracker_api.repository.UserRepository;
import org.pileka.fitness_tracker_api.security.JwtTokenProvider;
import org.pileka.fitness_tracker_api.service.impl.AuthServiceImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class AuthServiceImplTest {
    UserRepository userRepository;

    JwtTokenProvider jwtTokenProvider;

    // Mocking this even though I prefer to not mock what I don't own, but getting a real instance is impossible for a true unit test
    AuthenticationManager authenticationManager;

    AuthServiceImpl authService;

    PasswordEncoder passwordEncoder;

    private LoginRequestDto loginRequestDto;
    private RegistrationDto registrationDto;
    private TokenDto tokenDto;
    private User user;

    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final String BEARER_TOKEN = "bearer token";
    private static final String REFRESH_TOKEN = "refresh token";
    private static final Long BEARER_TOKEN_EXPIRATION = 900L;
    private static final Long REFRESH_TOKEN_EXPIRATION = 60L * 24 * 7;



    AuthServiceImplTest() {
        this.userRepository = mock(UserRepository.class);
        this.jwtTokenProvider = mock(JwtTokenProvider.class);
        this.authenticationManager = mock(AuthenticationManager.class);
        this.passwordEncoder = new BCryptPasswordEncoder();

        this.authService = new AuthServiceImpl(userRepository, passwordEncoder, jwtTokenProvider, authenticationManager);
    }

    @BeforeEach
    void setUpTestEntities() {
        this.loginRequestDto = new LoginRequestDto(USERNAME, PASSWORD);
        this.registrationDto = new RegistrationDto(USERNAME, PASSWORD, "cool@email.com");
        this.tokenDto = new TokenDto(BEARER_TOKEN, BEARER_TOKEN_EXPIRATION, REFRESH_TOKEN, REFRESH_TOKEN_EXPIRATION);

        this.user = new User();
        this.user.setUsername(USERNAME);
        this.user.setEmail("cool@email.com");
        this.user.setPassword(passwordEncoder.encode(PASSWORD));
    }

    private void setUpFullTokenDtoMock() {
        when(jwtTokenProvider.generateBearerToken(USERNAME)).thenReturn(BEARER_TOKEN);
        when(jwtTokenProvider.generateRefreshToken("user")).thenReturn(REFRESH_TOKEN);
        when(jwtTokenProvider.getBearerTokenExpiration()).thenReturn(BEARER_TOKEN_EXPIRATION);
        when(jwtTokenProvider.getRefreshTokenExpiration()).thenReturn(REFRESH_TOKEN_EXPIRATION);
    }

    private void verifyFullTokenDtoGeneration() {
        verify(jwtTokenProvider).generateBearerToken(USERNAME);
        verify(jwtTokenProvider).generateRefreshToken(USERNAME);
        verify(jwtTokenProvider).getBearerTokenExpiration();
        verify(jwtTokenProvider).getRefreshTokenExpiration();
    }

    private void verifyTokenDtoNotGenerated() {
        verify(jwtTokenProvider, never()).generateBearerToken(USERNAME);
        verify(jwtTokenProvider, never()).generateRefreshToken(USERNAME);
        verify(jwtTokenProvider, never()).getBearerTokenExpiration();
        verify(jwtTokenProvider, never()).getRefreshTokenExpiration();
    }

    @Test
    void registerAddsANewUser() {
        authService.register(registrationDto);

        verify(userRepository).save(any());
    }

    @Test
    void registerThrowsEntityRestrictionViolationException() {
        when(userRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        assertThrows(EntityRestrictionViolationException.class, () -> authService.register(registrationDto));
        verify(userRepository).save(any());
    }

    @Test
    void loginReturnsTokensForValidUser() {
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        setUpFullTokenDtoMock();

        TokenDto result = authService.login(loginRequestDto);

        assertEquals(tokenDto, result);

        verify(authenticationManager).authenticate(any());

        verify(userRepository).findByUsername("user");
        verifyFullTokenDtoGeneration();
    }

    @Test
    void loginThrowsUserLoginFailedException() {
        when(authenticationManager.authenticate(any())).thenAnswer(args -> {
            throw new AuthenticationException("") {}; // An anonymous instance of AuthenticationException
        });

        assertThrows(UserLoginFailedException.class, () -> authService.login(loginRequestDto));

        verify(userRepository, never()).findByUsername(USERNAME);

        verifyTokenDtoNotGenerated();
    }

    @Test
    void refreshReturnsNewTokens() {
        when(jwtTokenProvider.getUsernameFromToken(REFRESH_TOKEN)).thenReturn(USERNAME);
        when(jwtTokenProvider.tokenIsValid(REFRESH_TOKEN)).thenReturn(true);
        setUpFullTokenDtoMock();

        TokenDto result = authService.refresh(REFRESH_TOKEN);

        assertEquals(tokenDto, result);

        verify(jwtTokenProvider).getUsernameFromToken(REFRESH_TOKEN);
        verifyFullTokenDtoGeneration();
    }

    @Test
    void refreshThrowsRefreshTokenInvalidException() {
        when(jwtTokenProvider.tokenIsValid(REFRESH_TOKEN)).thenReturn(false);
        assertThrows(RefreshTokenInvalidException.class, () -> authService.refresh(REFRESH_TOKEN));

        verify(jwtTokenProvider, never()).getUsernameFromToken(REFRESH_TOKEN);
        verifyTokenDtoNotGenerated();
    }

}
