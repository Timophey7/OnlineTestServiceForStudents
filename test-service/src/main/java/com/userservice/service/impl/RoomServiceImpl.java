package com.userservice.service.impl;

import com.userservice.model.room.RoomResponse;
import com.userservice.model.room.StudentRoom;
import com.userservice.model.user.User;
import com.userservice.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final WebClient.Builder webClientBuilder;


    @Override
    public List<StudentRoom> findAllStudentRoomsByEmail(String email) {
        StudentRoom[] block = webClientBuilder.build().get().uri("http://security-service/v1/test/security/getRooms/" + email)
                .retrieve().bodyToMono(StudentRoom[].class).block();
        return Arrays.asList(block);
    }

//    @Override
//    public void saveRoom(RoomResponse room) {
//        webClientBuilder.build().post().uri("http://security-service/v1/test/security/saveUserInRoom").bodyValue(room)
//                .retrieve().bodyToMono(String.class).block();
//    }

    @Override
    public void createRoom(@Valid StudentRoom room) {
        webClientBuilder.build().post().uri("http://security-service/v1/test/security/createRoom")
                .bodyValue(room).retrieve().bodyToMono(String.class).block();
    }

    @Override
    @Cacheable(value = "Test-service:getStudentRoomInfoByUniqueCode", key = "#uniqueCode")
    public StudentRoom getStudentRoomInfoByUniqueCode(String uniqueCode) {
        StudentRoom studentRoom = webClientBuilder.build().get().uri("http://security-service/v1/test/security/getRoomInfo/" + uniqueCode)
                .retrieve().bodyToMono(StudentRoom.class).block();
        return studentRoom;
    }

    @Override
    public void saveStudentInRoom(String uniqueCode, User user) {
        webClientBuilder.build().post().uri("http://security-service/v1/test/security/saveStudentInRoom/"+uniqueCode)
                .bodyValue(user).retrieve().bodyToMono(String.class).block();
    }

    @Override
    public List<StudentRoom> getAllStudentsRooms(int studentId) {
        StudentRoom[] block = webClientBuilder.build().get().uri("http://security-service/v1/test/security/getStudentRooms/" + studentId)
                .retrieve().bodyToMono(StudentRoom[].class).block();
        return Arrays.asList(block);
    }
}
