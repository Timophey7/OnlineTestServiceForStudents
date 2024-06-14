package com.userservice.service.impl;

import com.userservice.model.test.Question;
import com.userservice.repository.QuestionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private EntityManager entityManager; // Создайте mock-объект

    @InjectMocks
    private QuestionServiceImpl questionService;

    @Test
    void updateByUniqueCode() {
        Question question = new Question();
        question.setUniqueCode("uniqueCode");
        question.setId(1);
        question.setText("How are you?");
        String uniqueCode = "uniqueCode";

        when(questionRepository.findQuestionByUniqueCode(uniqueCode)).thenReturn(question);
        when(entityManager.merge(question)).thenReturn(question);

        questionService.updateByUniqueCode(uniqueCode, question);

        verify(entityManager).merge(question);
    }

}