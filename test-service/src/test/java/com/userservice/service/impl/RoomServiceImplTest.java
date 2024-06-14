package com.userservice.service.impl;

import com.userservice.model.room.StudentRoom;
import com.userservice.model.user.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Incubating;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    WebClient.Builder webClientBuilder;
    @InjectMocks
    RoomServiceImpl roomService;

    StudentRoom studentRoom;

    @BeforeEach
    void setUp() {
        studentRoom = new StudentRoom();
        studentRoom.setRoomName("test");
        studentRoom.setRoomId(1);
        studentRoom.setOwnerEmail("test@test.com");
        studentRoom.setUniqueCode("uniqueCode");
    }

    @Test
    void findAllStudentRoomsByEmail() {

        String email = "test@test.com";
        StudentRoom[] studentRooms = new StudentRoom[] {studentRoom};

        WebClient mockWebClient = Mockito.mock(WebClient.class);
        WebClient.Builder mockBuilder = Mockito.mock(WebClient.Builder.class);
        WebClient.RequestHeadersUriSpec mockUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec mockResponseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(mockWebClient.get()).thenReturn(mockUriSpec);
        when(mockUriSpec.uri("http://security-service/v1/test/security/getRooms/" + email))
                .thenReturn(mockUriSpec);
        when(mockUriSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.bodyToMono(StudentRoom[].class)).thenReturn(Mono.just(studentRooms));

        when(webClientBuilder.build()).thenReturn(mockWebClient);

        List<StudentRoom> allStudentRoomsByEmail = roomService.findAllStudentRoomsByEmail(email);

        assertNotNull(allStudentRoomsByEmail);
        assertEquals(studentRooms.length, allStudentRoomsByEmail.size());
        assertEquals(studentRooms[0].getRoomName(), allStudentRoomsByEmail.get(0).getRoomName());


    }

    @Test
    void createRoom() {
        WebClient mockWebClient = Mockito.mock(WebClient.class);
        WebClient.Builder mockBuilder = Mockito.mock(WebClient.Builder.class);
        WebClient.RequestHeadersUriSpec mockUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestBodyUriSpec mockRequestBodyUriSpec = Mockito.mock(WebClient.RequestBodyUriSpec.class);
        WebClient.ResponseSpec mockResponseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(mockWebClient.post()).thenReturn(mockRequestBodyUriSpec);
        when(mockRequestBodyUriSpec.uri("http://security-service/v1/test/security/createRoom"))
                .thenReturn(mockRequestBodyUriSpec);
        when(mockRequestBodyUriSpec.bodyValue(studentRoom)).thenReturn(mockUriSpec);
        when(mockUriSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.bodyToMono(String.class)).thenReturn(Mono.just("success"));

        when(webClientBuilder.build()).thenReturn(mockWebClient);

        roomService.createRoom(studentRoom);

        verify(webClientBuilder).build();
    }

    @Test
    void getStudentRoomInfoByUniqueCode() {

        String uniqueCode = "uniqueCode";

        WebClient mockWebClient = Mockito.mock(WebClient.class);
        WebClient.Builder mockBuilder = Mockito.mock(WebClient.Builder.class);
        WebClient.RequestHeadersUriSpec mockUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec mockResponseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(mockWebClient.get()).thenReturn(mockUriSpec);
        when(mockUriSpec.uri("http://security-service/v1/test/security/getRoomInfo/" + uniqueCode))
                .thenReturn(mockUriSpec);
        when(mockUriSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.bodyToMono(StudentRoom.class)).thenReturn(Mono.just(studentRoom));

        when(webClientBuilder.build()).thenReturn(mockWebClient);

        StudentRoom studentRoomInfoByUniqueCode = roomService.getStudentRoomInfoByUniqueCode(uniqueCode);

        assertNotNull(studentRoomInfoByUniqueCode);
        assertEquals(studentRoom.getRoomName(), studentRoomInfoByUniqueCode.getRoomName());

    }



    @Test
    void saveStudentInRoom() {

        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setFirstName("test");
        user.setLastName("test");

        String uniqueCode = "uniqueCode";

        WebClient mockWebClient = Mockito.mock(WebClient.class);
        WebClient.Builder mockBuilder = Mockito.mock(WebClient.Builder.class);
        WebClient.RequestHeadersUriSpec mockUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestBodyUriSpec mockRequestBodyUriSpec = Mockito.mock(WebClient.RequestBodyUriSpec.class);
        WebClient.ResponseSpec mockResponseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(mockWebClient.post()).thenReturn(mockRequestBodyUriSpec);
        when(mockRequestBodyUriSpec.uri("http://security-service/v1/test/security/saveStudentInRoom/" + uniqueCode))
                .thenReturn(mockRequestBodyUriSpec);
        when(mockRequestBodyUriSpec.bodyValue(user)).thenReturn(mockUriSpec);
        when(mockUriSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.bodyToMono(String.class)).thenReturn(Mono.just("success"));

        when(webClientBuilder.build()).thenReturn(mockWebClient);

        roomService.saveStudentInRoom(uniqueCode,user);

        verify(webClientBuilder).build();
    }

    @Test
    void getAllStudentsRooms() {
        int studentId = 1;
        StudentRoom[] studentRooms = new StudentRoom[] {studentRoom};

        WebClient mockWebClient = Mockito.mock(WebClient.class);
        WebClient.Builder mockBuilder = Mockito.mock(WebClient.Builder.class);
        WebClient.RequestHeadersUriSpec mockUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec mockResponseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(mockWebClient.get()).thenReturn(mockUriSpec);
        when(mockUriSpec.uri("http://security-service/v1/test/security/getStudentRooms/"+studentId)).thenReturn(mockUriSpec);
        when(mockUriSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.bodyToMono(StudentRoom[].class)).thenReturn(Mono.just(studentRooms));

        when(webClientBuilder.build()).thenReturn(mockWebClient);

        List<StudentRoom> allStudentsRooms = roomService.getAllStudentsRooms(studentId);

        assertNotNull(allStudentsRooms);
        assertEquals(studentRooms.length, allStudentsRooms.size());

    }
}