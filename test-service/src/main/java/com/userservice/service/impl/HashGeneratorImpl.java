package com.userservice.service.impl;


import com.userservice.service.HashGenerator;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Random;

@Service
public class HashGeneratorImpl implements HashGenerator {
    @Override
    public String generateHash() {
        byte[] randomBytes = new byte[6];
        new Random().nextBytes(randomBytes);
        String base64Encoded = Base64.getEncoder().encodeToString(randomBytes);
        if (base64Encoded.contains("/")){
            StringBuilder sb = new StringBuilder(base64Encoded);
            int index = sb.indexOf("/");
            while (index != -1) {
                sb.deleteCharAt(index);
                index = sb.indexOf("/");
            }
            return sb.toString();

        }
        return base64Encoded.substring(0, 8);
    }
}
