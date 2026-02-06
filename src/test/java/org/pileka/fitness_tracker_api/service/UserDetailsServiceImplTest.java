package org.pileka.fitness_tracker_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pileka.fitness_tracker_api.domain.User;
import org.pileka.fitness_tracker_api.repository.UserRepository;
import org.pileka.fitness_tracker_api.service.impl.UserDetailsServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.swing.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    private User user;

    private static final String USERNAME = "user";

    @BeforeEach
    void setUpTestEntities() {
        this.user = new User();
        this.user.setUsername(USERNAME);
        this.user.setEmail("cool@email.com");
        this.user.setPassword("coolpassword");
    }

    @Test
    void loadUserByUsernameLoadsUserDetails() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.ofNullable(user));

        UserDetails result = userDetailsService.loadUserByUsername(USERNAME);

        assertEquals(result, user);

        verify(userRepository).findByUsername(USERNAME);
    }

    @Test
    void loadUserByUsernameThrowsUsernameNotFoundException() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(USERNAME));

        verify(userRepository).findByUsername(USERNAME);
    }
}
