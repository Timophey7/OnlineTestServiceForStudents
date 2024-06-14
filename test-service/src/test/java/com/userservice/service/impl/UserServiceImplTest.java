package com.userservice.service.impl;

import com.userservice.model.user.AuthenticationRequest;
import com.userservice.model.user.AuthenticationResponse;
import com.userservice.model.user.User;
import com.userservice.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private WebClient.Builder webClientBuilder;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findUserByToken_shouldReturnUser_whenTokenIsValid() {
        HttpSession httpSession = new MockHttpSession();
        httpSession.setAttribute("token", "token");

        User user = new User();
        user.setEmail("email");
        user.setPassword("password");
        user.setFirstName("firstName");
        user.setLastName("lastName");

        WebClient mockWebClient = Mockito.mock(WebClient.class);
        WebClient.Builder mockBuilder = Mockito.mock(WebClient.Builder.class);
        WebClient.RequestHeadersUriSpec mockUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec mockResponseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(mockWebClient.get()).thenReturn(mockUriSpec);
        when(mockUriSpec.uri("http://security-service/v1/test/security/getUserByToken")).thenReturn(mockUriSpec);
        when(mockUriSpec.header("Authorization", "Bearer " + httpSession.getAttribute("token"))).thenReturn(mockUriSpec);
        when(mockUriSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.bodyToMono(User.class)).thenReturn(Mono.just(user));

        when(webClientBuilder.build()).thenReturn(mockWebClient);

        User userByToken = userService.findUserByToken(httpSession);

        assertNotNull(userByToken);
        assertEquals("email", userByToken.getEmail());
        assertEquals("password", userByToken.getPassword());
    }

    @Test
    void registration() {
        HttpSession httpSession = new MockHttpSession();

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken("token");

        User user = new User();
        user.setEmail("email");
        user.setPassword("password");
        user.setFirstName("firstName");
        user.setLastName("lastName");

        WebClient mockWebClient = Mockito.mock(WebClient.class);
        WebClient.Builder mockBuilder = Mockito.mock(WebClient.Builder.class);
        WebClient.RequestHeadersUriSpec mockUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestBodyUriSpec mockRequestBodyUriSpec = Mockito.mock(WebClient.RequestBodyUriSpec.class);
        WebClient.ResponseSpec mockResponseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(mockWebClient.post()).thenReturn(mockRequestBodyUriSpec);
        when(mockRequestBodyUriSpec.uri("http://security-service/v1/test/security/registration")).thenReturn(mockRequestBodyUriSpec);
        when(mockRequestBodyUriSpec.bodyValue(user)).thenReturn(mockUriSpec);
        when(mockUriSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.bodyToMono(AuthenticationResponse.class)).thenReturn(Mono.just(authenticationResponse));

        when(webClientBuilder.build()).thenReturn(mockWebClient);

        AuthenticationResponse registration = userService.registration(user, httpSession);

        assertNotNull(registration);
        assertEquals("token", registration.getToken());
    }


    @Test
    void verify() {

        HttpSession httpSession = new MockHttpSession();

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken("token");

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("email");
        authenticationRequest.setPassword("password");

        WebClient mockWebClient = Mockito.mock(WebClient.class);
        WebClient.Builder mockBuilder = Mockito.mock(WebClient.Builder.class);
        WebClient.RequestHeadersUriSpec mockUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestBodyUriSpec mockRequestBodyUriSpec = Mockito.mock(WebClient.RequestBodyUriSpec.class);
        WebClient.ResponseSpec mockResponseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(mockWebClient.post()).thenReturn(mockRequestBodyUriSpec);
        when(mockRequestBodyUriSpec.uri("http://security-service/v1/test/security/verify")).thenReturn(mockRequestBodyUriSpec);
        when(mockRequestBodyUriSpec.bodyValue(authenticationRequest)).thenReturn(mockUriSpec);
        when(mockUriSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.bodyToMono(AuthenticationResponse.class)).thenReturn(Mono.just(authenticationResponse));

        when(webClientBuilder.build()).thenReturn(mockWebClient);

        String verify = userService.verify(authenticationRequest, httpSession);

        assertNotNull(verify);

    }
}