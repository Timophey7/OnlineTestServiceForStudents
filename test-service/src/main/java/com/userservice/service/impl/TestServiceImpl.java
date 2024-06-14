package com.userservice.service.impl;

import com.userservice.model.test.Test;
import com.userservice.repository.TestRepository;
import com.userservice.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;


    @Override
//    @Cacheable(value = "Test-service:findTestByUniqueCode", key = "#uniqueCode")
    public Test findTestByUniqueCode(String uniqueCode) {
        return testRepository.findTestByUniqueCode(uniqueCode);
    }

    @Override
    public Optional<List<Test>> findAllByCreatorEmail(String email) {
        return testRepository.findAllByCreatorEmail(email);
    }

    @Override
    public int findTestIdByQuestionId(int questionId) {
        return testRepository.findTestIdByQuestionId(questionId);
    }

    @Override
    public void save(Test test) {
        testRepository.save(test);
    }

    @Override
    @Cacheable(value = "Test-service:findById", key = "#id")
    public Test findById(int id) {
        return testRepository.findById(id).orElse(null);
    }

    @Override
    public List<Test> findAll() {
        return testRepository.findAll();
    }
}
