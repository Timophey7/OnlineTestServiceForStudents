package com.userservice.service;


import com.userservice.model.room.RoomResponse;
import com.userservice.model.room.StudentRoom;
import com.userservice.model.user.User;
import jakarta.validation.Valid;

import java.util.List;

public interface RoomService {

    List<StudentRoom> findAllStudentRoomsByEmail(String email);

//    void saveRoom(RoomResponse room);

    void createRoom(@Valid StudentRoom room);

    StudentRoom getStudentRoomInfoByUniqueCode(String uniqueCode);

    void saveStudentInRoom(String uniqueCode, User user);

    List<StudentRoom> getAllStudentsRooms(int studentId);

}
