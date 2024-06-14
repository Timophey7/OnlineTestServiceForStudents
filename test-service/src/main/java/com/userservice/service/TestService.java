package com.userservice.service;

import com.userservice.model.test.Test;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TestService {

    Test findTestByUniqueCode(String uniqueCode);

    Optional<List<Test>> findAllByCreatorEmail(String email);

    int findTestIdByQuestionId(int questionId);

    void save(Test test);

    Test findById(int id);

    List<Test> findAll();

}
