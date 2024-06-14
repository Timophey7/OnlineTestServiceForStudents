package com.notificationservice.service.imol;

import com.notificationservice.service.NotificationService;
import io.micrometer.core.annotation.Timed;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final MimeMessageHelper helper;
    private final JavaMailSender mailSender;

    @Override
    @Timed("sendEmailMethod")
    public void sendEmail(String email) {
        try{
            helper.setFrom("auction@gmail.com");
            helper.setTo(email);
            helper.setSubject("online test");
            helper.setText("Уже больше семи учеников прошли ваш тест!");
            mailSender.send(helper.getMimeMessage());
        }catch (MessagingException e){
            e.printStackTrace();
        }
    }
}
