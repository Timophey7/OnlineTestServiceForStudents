package com.securityservice.service.impl;

import com.securityservice.model.StudentRoom;
import com.securityservice.reposiroty.StudentRoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private StudentRoomRepository studentRoomRepository;

    @InjectMocks
    private RoomServiceImpl roomServiceImpl;

    @Test
    void getAllStudentRoomsShouldReturnAllStudentRooms() {
        StudentRoom studentRoom = new StudentRoom();
        studentRoom.setRoomName("test");
        List<Integer> integerList = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        when(studentRoomRepository.findAllRoomsIds(1)).thenReturn(integerList);
        when(studentRoomRepository.findById(any())).thenReturn(Optional.of(studentRoom));

        List<StudentRoom> allStudentRooms = roomServiceImpl.getAllStudentRooms(1);

        assertNotNull(allStudentRooms);
        assertEquals(allStudentRooms.size(), integerList.size());

    }

    @Test
    void getAllStudentRoomsShouldReturnEmptyList() {
        List<Integer> integerList = List.of();
        when(studentRoomRepository.findAllRoomsIds(1)).thenReturn(integerList);

        List<StudentRoom> allStudentRooms = roomServiceImpl.getAllStudentRooms(1);

        assertEquals(0, allStudentRooms.size());

    }
}