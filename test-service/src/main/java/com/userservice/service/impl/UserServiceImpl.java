package com.userservice.service.impl;

import com.userservice.model.user.AuthenticationRequest;
import com.userservice.model.user.AuthenticationResponse;
import com.userservice.model.user.User;
import com.userservice.model.user.VerifyResponse;
import com.userservice.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final WebClient.Builder webClientBuilder;

    @Override
    public User findUserByToken(HttpSession session) {
        return webClientBuilder.build().get().uri("http://security-service/v1/test/security/getUserByToken")
                .header("Authorization", "Bearer " + session.getAttribute("token"))
                .retrieve().bodyToMono(User.class).block();

    }

    @Override
    public AuthenticationResponse registration(User user, HttpSession session) {
        AuthenticationResponse token = webClientBuilder.build().post().uri("http://security-service/v1/test/security/registration")
                .bodyValue(user).retrieve().bodyToMono(AuthenticationResponse.class).block();
        log.info("token: {}", token);
        session.setAttribute("token", token.getToken());
        return token;
    }

    @Override
    public String verify(AuthenticationRequest authenticationRequest,HttpSession session) {
        AuthenticationResponse authenticationResponse = webClientBuilder.build().post().uri("http://security-service/v1/test/security/verify")
                .bodyValue(authenticationRequest).retrieve().bodyToMono(AuthenticationResponse.class).block();
        if (authenticationResponse.getToken() != null){
            log.info("token: {}", authenticationResponse.getToken());
            session.setAttribute("token", authenticationResponse.getToken());
            return authenticationResponse.getToken();
        }
        return null;
    }
}
