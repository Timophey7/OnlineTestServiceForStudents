package com.securityservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.securityservice.model.*;
import com.securityservice.service.JwtService;
import com.securityservice.service.RoomService;
import com.securityservice.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = SecurityController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;
    @MockBean
    private RoomService roomService;
    @MockBean
    private UserService userService;
    @MockBean
    private PasswordEncoder passwordEncoder;

    User user;
    StudentRoom studentRoom;
    @BeforeEach
    void setUp() {

        user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("test@gmail.com");
        user.setRole(Role.STUDENT);
        studentRoom = new StudentRoom();
        studentRoom.setRoomName("test");
        studentRoom.setRoomId(1);
        studentRoom.setUniqueCode("code");
        studentRoom.setOwnerEmail("test@gmail.com");;
    }

    @Test
    void registerUser() throws Exception {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken("token");
        when(userService.saveUser(user)).thenReturn(authenticationResponse);

        ResultActions perform = mockMvc.perform(post("/v1/test/security/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
        );

        perform.andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("token"));

    }

    @Test
    void getUserByTokenShouldReturnUser() throws Exception {

        when(userService.findUserByToken("token")).thenReturn(user);

        ResultActions perform = mockMvc.perform(get("/v1/test/security/getUserByToken")
                .header("Authorization", "Bearer token"));

        perform.andExpect(status().isOk())
                .andDo( print())
                .andExpect(jsonPath("$..email").value(user.getEmail()));

    }



    @Test
    void verifyUserShouldReturnToken() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("test@gmail.com");
        authenticationRequest.setPassword("password");
        when(userService.findUserByEmail("test@gmail.com")).thenReturn(user);
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("token");

        mockMvc.perform(post("/v1/test/security/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));

    }

    @Test
    void verifyUserShouldReturnNull() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("test@gmail.com");
        authenticationRequest.setPassword("password");
        when(userService.findUserByEmail("test@gmail.com")).thenReturn(user);
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(false);
        when(jwtService.generateToken(user)).thenReturn("token");

        mockMvc.perform(post("/v1/test/security/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andDo(print());

    }


    @Test
    void createRoomShouldReturnStatusIsOk() throws Exception {
        doNothing().when(roomService).save(studentRoom);

        mockMvc.perform(post("/v1/test/security/createRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentRoom))
        )
                .andExpect(status().isOk());
        verify(roomService, times(1)).save(studentRoom);
    }



    @Test
    void getRoomsShouldReturnStatusIsOk() throws Exception {
        String ownerEmail = "owner@gmail.com";
        List<StudentRoom> studentRoomList = List.of(studentRoom);
        when(roomService.findAllByOwnerEmail(ownerEmail)).thenReturn(studentRoomList);

        ResultActions perform = mockMvc.perform(get("/v1/test/security/getRooms/" + ownerEmail)
                .param("ownerEmail", ownerEmail));

        perform.andExpect(status().isOk());
    }

    @Test
    void getRoomInfoShouldReturnStatusIsOk() throws Exception {

        when(roomService.findByUniqueCode("code")).thenReturn(Optional.of(studentRoom));

        mockMvc.perform(get("/v1/test/security/getRoomInfo/"+"code")
                .param("uniqueCode","code"))
                .andExpect(status().isOk());

    }

    @Test
    void saveStudentInRoomShouldReturnStatusIsOk() throws Exception {

        when(roomService.findByUniqueCode("code")).thenReturn(Optional.of(studentRoom));
        doNothing().when(roomService).save(studentRoom);

        ResultActions perform = mockMvc.perform(post("/v1/test/security/saveStudentInRoom/" + "code")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentRoom))
                .param("uniqueCode", "code")
        );

        perform.andExpect(status().isOk());
        verify(roomService, times(1)).save(studentRoom);

    }

    @Test
    void getStudentRoomsShouldReturnStatusIsOk() throws Exception {
        int studentId = 1;
        List<StudentRoom> studentRoomList = List.of(studentRoom);
        when(roomService.getAllStudentRooms(studentId)).thenReturn(studentRoomList);

        mockMvc.perform(get("/v1/test/security/getStudentRooms/"+studentId)
                .param("studentId",String.valueOf(studentId)))
                .andExpect(status().isOk());
    }
}