package com.userservice.controller;

import com.userservice.model.user.AuthenticationRequest;
import com.userservice.model.user.AuthenticationResponse;
import com.userservice.model.user.User;
import com.userservice.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.checkerframework.checker.units.qual.Acceleration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@WebMvcTest(controllers = SecurityController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void registerHowStudentShouldReturnStatusIsOk() throws Exception {

        ResultActions perform = mockMvc.perform(get("/v1/test/security/register"));
        perform.andExpect(status().isOk())
                .andExpect(model().attributeExists("user"));

    }

    @Test
    void sendUserInfoShouldReturnStatusIsRedirect() throws Exception {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken("token");
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setIsTeacher(true);
        HttpSession session = new MockHttpSession();

        when(userService.registration(user,session)).thenReturn(authenticationResponse);

        ResultActions perform = mockMvc.perform(post("/v1/test/security/register")
                .requestAttr("user", user));

        perform.andExpect(status().is3xxRedirection());


    }

    @Test
    void loginShouldReturnStatusIsOk() throws Exception {

        ResultActions perform = mockMvc.perform(get("/v1/test/security/login"));

        perform.andExpect(status().isOk())
                .andExpect(model().attributeExists("authentication"));


    }

    @Test
    void verification() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("test@test.com");
        authenticationRequest.setPassword("password");
        HttpSession session = new MockHttpSession();

        when(userService.verify(authenticationRequest,session)).thenReturn("test@test.com");

        ResultActions perform = mockMvc.perform(get("/v1/test/security/verification")
                .requestAttr("authentication", authenticationRequest));

        perform.andExpect(status().is3xxRedirection())
                .andDo(print());

    }
}