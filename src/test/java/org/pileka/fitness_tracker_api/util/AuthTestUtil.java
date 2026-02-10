package org.pileka.fitness_tracker_api.util;

import lombok.experimental.UtilityClass;
import org.mockito.MockedStatic;
import org.pileka.fitness_tracker_api.dto.auth.LoginRequestDto;
import org.pileka.fitness_tracker_api.dto.auth.RegistrationDto;
import org.pileka.fitness_tracker_api.dto.auth.TokenDto;
import org.pileka.fitness_tracker_api.security.AuthUserUtil;
import org.pileka.fitness_tracker_api.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Supplier;

import static org.mockito.Mockito.mockStatic;
import static org.pileka.fitness_tracker_api.util.UserTestUtil.PASSWORD;
import static org.pileka.fitness_tracker_api.util.UserTestUtil.USERNAME;

@UtilityClass
public class AuthTestUtil {
    public final UserDetails testUserDetails = new CustomUserDetails(USERNAME, PASSWORD);

    public final String BEARER_TOKEN = "bearer token";
    public final String REFRESH_TOKEN = "refresh token";
    public final Long BEARER_TOKEN_EXPIRATION = 900L;
    public final Long REFRESH_TOKEN_EXPIRATION = 60L * 24 * 7;

    public final LoginRequestDto testLoginRequestDto = new LoginRequestDto(USERNAME, PASSWORD);
    public final RegistrationDto testRegistrationDto = new RegistrationDto(USERNAME, PASSWORD, "cool@email.com");
    public final TokenDto testTokenDto = new TokenDto(BEARER_TOKEN, BEARER_TOKEN_EXPIRATION, REFRESH_TOKEN, REFRESH_TOKEN_EXPIRATION);

    public static <T> T doWithMockedAuthUserUtil(Supplier<T> testCode) {
        try (MockedStatic<AuthUserUtil> authUserUtilMock = mockStatic(AuthUserUtil.class)) {
            authUserUtilMock.when(AuthUserUtil::getCurrentUser).thenReturn(testUserDetails);

            T result = testCode.get();

            authUserUtilMock.verify(AuthUserUtil::getCurrentUser);

            return result;
        }
    }

    public static void doWithMockedAuthUserUtil(Runnable testCode) {
        try (MockedStatic<AuthUserUtil> authUserUtilMock = mockStatic(AuthUserUtil.class)) {
            authUserUtilMock.when(AuthUserUtil::getCurrentUser).thenReturn(testUserDetails);

            testCode.run();

            authUserUtilMock.verify(AuthUserUtil::getCurrentUser);
        }
    }
}
