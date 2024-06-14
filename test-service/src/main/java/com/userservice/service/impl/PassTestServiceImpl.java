package com.userservice.service.impl;

import com.userservice.model.test.PassedTest;
import com.userservice.repository.PassedTestRepository;
import com.userservice.service.PassTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassTestServiceImpl implements PassTestService {

    private final PassedTestRepository passedTestRepository;

    @Override
    public int calculatePercent(int rightAnswers, int allQuestions) {
        return  (rightAnswers * 100) / allQuestions;
    }

    @Override
    public String grade(int percent) {

        if (percent < 50){
            return "2";
        }
        if (percent < 70 && percent>50){
            return "3";
        }
        if (percent < 85 && percent>70){
            return "4";
        }
        return "5";
    }

    @Override
//    @Cacheable(value = "Test-service:findByStudentEmailAndTestUniqueCode", key = "#studentEmail + '.' + #testUniqueCode")
    public PassedTest findByStudentEmailAndTestUniqueCode(String studentEmail, String testUniqueCode) {
        return passedTestRepository.findByStudentEmailAndTestUniqueCode(studentEmail, testUniqueCode);
    }

    @Override
    public List<PassedTest> findAllByTestUniqueCode(String testUniqueCode) {
        return passedTestRepository.findAllByTestUniqueCode(testUniqueCode);
    }

    @Override
//    @CachePut(value = "Test-service:findByStudentEmailAndTestUniqueCode", key = "#passedTest.studentEmail + '.' + #passedTest.testUniqueCode")
    public void savePassedTestInfo(PassedTest passedTest) {
        passedTestRepository.save(passedTest);
    }


}
