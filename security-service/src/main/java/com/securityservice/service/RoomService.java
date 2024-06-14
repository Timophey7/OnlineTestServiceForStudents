package com.securityservice.service;

import com.securityservice.model.StudentRoom;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    List<StudentRoom> getAllStudentRooms(Integer studentId);

    List<StudentRoom> findAllByOwnerEmail(String email);

    Optional<StudentRoom> findByUniqueCode(String uniqueCode);

    void save(StudentRoom studentRoom);

}
