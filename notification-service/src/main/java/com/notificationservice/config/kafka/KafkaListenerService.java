package com.notificationservice.config.kafka;

import com.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaListenerService {

    private final NotificationService notificationService;

    @KafkaListener(topics = "email",groupId = "notification-system")
    public void listen(String email) {
        notificationService.sendEmail(email);

    }

}
