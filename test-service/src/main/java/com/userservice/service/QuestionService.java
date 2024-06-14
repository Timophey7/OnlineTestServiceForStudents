package com.userservice.service;

import com.userservice.model.test.Question;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionService {

    public void updateByUniqueCode(String uniqueCode, Question question);


    Question findQuestionByUniqueCode(String uniqueCode);


    List<Question> findQuestionByTestId( int id);

    void save(Question question);

}
