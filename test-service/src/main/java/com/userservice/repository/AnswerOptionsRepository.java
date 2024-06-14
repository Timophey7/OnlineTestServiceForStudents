package com.userservice.repository;

import com.userservice.model.test.AnswerOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerOptionsRepository extends JpaRepository<AnswerOptions,Integer> {
}
