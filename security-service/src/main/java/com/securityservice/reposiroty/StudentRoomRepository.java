package com.securityservice.reposiroty;

import com.securityservice.model.StudentRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRoomRepository extends JpaRepository<StudentRoom, Integer> {

    List<StudentRoom> findAllByOwnerEmail(String email);

    Optional<StudentRoom> findByUniqueCode(String uniqueCode);

    @Query(value = "SELECT student_room_room_id from student_room_user_list where user_list_id = :studentId", nativeQuery = true)
    List<Integer> findAllRoomsIds(@Param("studentId") Integer studentId);

}
