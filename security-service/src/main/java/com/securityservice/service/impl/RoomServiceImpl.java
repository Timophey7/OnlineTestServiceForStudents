package com.securityservice.service.impl;

import com.securityservice.model.StudentRoom;
import com.securityservice.reposiroty.StudentRoomRepository;
import com.securityservice.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final StudentRoomRepository studentRoomRepository;

    @Override
    public List<StudentRoom> getAllStudentRooms(Integer studentId) {
        List<Integer> allRoomsIds = studentRoomRepository.findAllRoomsIds(studentId);
        List<StudentRoom> studentRooms = new ArrayList<>();
        for (Integer id : allRoomsIds) {
            StudentRoom studentRoom = studentRoomRepository.findById(id).orElse(null);
            studentRooms.add(studentRoom);
        }
        return studentRooms;
    }

    @Override
    public List<StudentRoom> findAllByOwnerEmail(String email) {
        return studentRoomRepository.findAllByOwnerEmail(email);
    }

    @Override
    @Cacheable(value = "Security-service:findByUniqueCode",key = "#uniqueCode")
    public Optional<StudentRoom> findByUniqueCode(String uniqueCode) {
        return studentRoomRepository.findByUniqueCode(uniqueCode);
    }

    @Override
    public void save(StudentRoom studentRoom) {
        studentRoomRepository.save(studentRoom);
    }
}
