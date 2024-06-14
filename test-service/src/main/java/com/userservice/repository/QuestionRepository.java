package com.userservice.repository;

import com.userservice.model.test.Question;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {


    Question findQuestionByUniqueCode(String uniqueCode);


    @Query(value = "select * from questions where test_id = :id", nativeQuery = true)
    @Transactional
    List<Question> findQuestionByTestId(@Param("id") int id);



}
