package com.userservice.controller;

import com.userservice.model.room.StudentRoom;
import com.userservice.model.user.User;
import com.userservice.service.HashGenerator;
import com.userservice.service.RoomService;
import com.userservice.service.TestService;
import com.userservice.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = RoomController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RoomService roomService;
    @MockBean
    private UserService userService;
    @MockBean
    private TestService testService;
    @MockBean
    private HashGenerator hashGenerator;

    User user;
    StudentRoom studentRoom;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setIsTeacher(true);
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@mail.com");
        user.setPhone("phone");
        studentRoom = new StudentRoom();
        studentRoom.setRoomName("test");
        studentRoom.setRoomId(1);
        studentRoom.setOwnerEmail("email@mail.com");
        studentRoom.setUniqueCode("uniqueCode");
    }

    @Test
    void homeShouldReturnStatusIsOk() throws Exception {

        com.userservice.model.test.Test test = new com.userservice.model.test.Test();
        test.setId(1);
        test.setName("test");

        when(testService.findAll()).thenReturn(List.of(test));

        ResultActions perform = mockMvc.perform(get("/v1/test/home"));

        perform.andExpect(status().isOk())
                .andExpect(model().attribute("tests",List.of(test)));

    }

    @Test
    void personalAreaWhereUserIsTeacherAndShouldReturnStatusIsOk() throws Exception {
        HttpSession session = new MockHttpSession();
        session.setAttribute("email","email@mail.com");
        when(userService.findUserByToken(session)).thenReturn(user);
        when(roomService.findAllStudentRoomsByEmail(user.getEmail())).thenReturn(List.of(studentRoom));

        ResultActions perform = mockMvc.perform(get("/v1/test/personalArea"));

        perform.andExpect(status().isOk())
                .andExpect(model().attribute("rooms",List.of(studentRoom)))
                .andExpect(model().attribute("user",user));
    }

    @Test
    void createRoomShouldReturnStatusIsOk() throws Exception {

        ResultActions perform = mockMvc.perform(get("/v1/test/createRoom"));

        perform.andExpect(status().isOk())
                .andExpect(model().attributeExists("room"));

    }

    @Test
    void saveRoomShouldReturnStatusIsRedirection() throws Exception {
       doNothing().when(roomService).createRoom(studentRoom);

        ResultActions perform = mockMvc.perform(post("/v1/test/createRoom")
                .requestAttr("room", studentRoom));

        perform.andExpect(status().is3xxRedirection());

    }

    @Test
    void roomInfo() throws Exception {
        user.setIsTeacher(false);
        HttpSession session = new MockHttpSession();
        com.userservice.model.test.Test test = new com.userservice.model.test.Test();
        String uniqueCode = "uniqueCode";
        when(roomService.getStudentRoomInfoByUniqueCode(uniqueCode)).thenReturn(studentRoom);
        when(testService.findAllByCreatorEmail(user.getEmail()))
                .thenReturn(Optional.of(List.of(test)));
        when(userService.findUserByToken(session)).thenReturn(user);

        ResultActions perform = mockMvc.perform(get("/v1/test/roomInfo/" + uniqueCode)
                .param("uniqueCode", uniqueCode));

        perform.andExpect(status().isOk())
                .andExpect(model().attribute("tests",List.of(test)))
                .andExpect(model().attribute("user",user));

    }

    @Test
    void entrance() throws Exception {

        ResultActions perform = mockMvc.perform(get("/v1/test/entrance"));

        perform.andExpect(status().isOk())
        .andExpect(model().attributeExists("roomUniqueCode"));

    }

    @Test
    void comeInRoom() {
    }
}