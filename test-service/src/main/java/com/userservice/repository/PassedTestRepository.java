package com.userservice.repository;

import com.userservice.model.test.PassedTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassedTestRepository extends JpaRepository<PassedTest, Integer> {

//    Optional<PassedTest> findByTestUniqueCodeAndStudentEmail(String testUniqueCode, String studentEmail);
//
//    PassedTest findByStudentEmail(String studentEmail);

    PassedTest findByStudentEmailAndTestUniqueCode(String studentEmail, String testUniqueCode);

    List<PassedTest> findAllByTestUniqueCode(String testUniqueCode);


}
