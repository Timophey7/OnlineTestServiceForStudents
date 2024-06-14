package com.securityservice.service.impl;


import com.securityservice.model.AuthenticationResponse;
import com.securityservice.model.User;
import com.securityservice.reposiroty.UserRepository;
import com.securityservice.service.JwtService;
import com.securityservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        log.info("Generated token: {}", token);
        return AuthenticationResponse.builder().token(token).build();
    }

    @Override
    @Cacheable(value = "Security-service:findUserByToken",key = "#token")
    public User findUserByToken(String token) {
        String email = jwtService.extractUsername(token);
        return userRepository.findUserByEmail(email).orElse(null);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElse(null);
    }
}
