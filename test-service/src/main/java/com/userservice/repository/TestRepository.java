package com.userservice.repository;

import com.userservice.model.test.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {

    Test findTestByUniqueCode(String uniqueCode);

    Optional<List<Test>> findAllByCreatorEmail(String email);

    @Query(value = "SELECT test_id FROM tests_questions where questions_id = :questionId",nativeQuery = true)
    int findTestIdByQuestionId(@Param("questionId") int questionId);
}
