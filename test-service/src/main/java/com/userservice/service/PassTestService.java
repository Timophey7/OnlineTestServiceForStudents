package com.userservice.service;

import com.userservice.model.test.PassedTest;

import java.util.List;

public interface PassTestService {

    int calculatePercent(int rightAnswers, int allQuestions);

    String grade(int percent);

    PassedTest findByStudentEmailAndTestUniqueCode(String studentEmail, String testUniqueCode);

    List<PassedTest> findAllByTestUniqueCode(String testUniqueCode);

    void savePassedTestInfo(PassedTest passedTest);
}
