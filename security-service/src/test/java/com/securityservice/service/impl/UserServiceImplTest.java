package com.securityservice.service.impl;

import com.securityservice.model.AuthenticationResponse;
import com.securityservice.model.User;
import com.securityservice.reposiroty.UserRepository;
import com.securityservice.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void saveUser() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        when(userRepository.save(user)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("token");

        AuthenticationResponse authenticationResponse = userServiceImpl.saveUser(user);

        assertNotNull(authenticationResponse);
        assertEquals("token", authenticationResponse.getToken());
        verify(userRepository, times(1)).save(user);

    }

    @Test
    void findUserByToken(){
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        String token = "token";
        when(jwtService.extractUsername(token)).thenReturn("test@gmail.com");
        when(userRepository.findUserByEmail("test@gmail.com")).thenReturn(Optional.of(user));

        User userFound = userServiceImpl.findUserByToken(token);

        assertNotNull(userFound);
        assertEquals(user, userFound);
    }
}