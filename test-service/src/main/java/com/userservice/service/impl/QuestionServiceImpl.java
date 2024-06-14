package com.userservice.service.impl;

import com.userservice.model.test.Question;
import com.userservice.repository.QuestionRepository;
import com.userservice.service.QuestionService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Modifying
    @Transactional
    public void updateByUniqueCode(String uniqueCode, Question question) {
        Question existingQuestion = questionRepository.findQuestionByUniqueCode(uniqueCode);
        if (existingQuestion != null) {
            Question updatedQuestion = existingQuestion;
            updatedQuestion.setText(question.getText());
            updatedQuestion.setType(question.getType());
            entityManager.merge(updatedQuestion);
        }
    }

    @Override
    public Question findQuestionByUniqueCode(String uniqueCode) {
        Question question = questionRepository.findQuestionByUniqueCode(uniqueCode);
        return question;
    }

    @Override
    public List<Question> findQuestionByTestId(int id) {
        return questionRepository.findQuestionByTestId(id);
    }

    @Override
    public void save(Question question) {
        questionRepository.save(question);
    }


}
