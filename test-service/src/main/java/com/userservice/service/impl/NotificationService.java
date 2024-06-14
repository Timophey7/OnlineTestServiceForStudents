package com.userservice.service.impl;

import com.userservice.model.test.PassedTest;
import com.userservice.model.test.Test;
import com.userservice.repository.PassedTestRepository;
import com.userservice.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final KafkaTemplate<String,String> kafkaTemplate;
    private final PassedTestRepository passedTestRepository;
    private final TestRepository testRepository;

    @Scheduled(fixedRate = 60000)
    public void scheduledNotification() {
        List<PassedTest> all = passedTestRepository.findAll();
        for (PassedTest passedTest : all) {
            String testUniqueCode = passedTest.getTestUniqueCode();
            int size = passedTestRepository.findAllByTestUniqueCode(testUniqueCode).size();
            if (size > 5){
                Test test = testRepository.findTestByUniqueCode(testUniqueCode);
                String creatorEmail = test.getCreatorEmail();
                kafkaTemplate.send("email",creatorEmail);
            }
        }
    }


}
