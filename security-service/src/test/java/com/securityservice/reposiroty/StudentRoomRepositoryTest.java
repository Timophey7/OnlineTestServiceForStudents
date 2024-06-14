package com.securityservice.reposiroty;

import com.securityservice.model.StudentRoom;
import com.securityservice.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StudentRoomRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRoomRepository studentRoomRepository;

    @Test
    void findAllRoomsIds() {
        StudentRoom studentRoom = new StudentRoom();
        studentRoom.setRoomId(1);
        studentRoom.setRoomName("test");
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("password");
        userRepository.save(user);
        studentRoom.addStudentInRoom(user);
        studentRoomRepository.save(studentRoom);

        List<Integer> allRoomsIds = studentRoomRepository.findAllRoomsIds((int) user.getId());

        assertTrue(allRoomsIds.size() == 1);
    }
}