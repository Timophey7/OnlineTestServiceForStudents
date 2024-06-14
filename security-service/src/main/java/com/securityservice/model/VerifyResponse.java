package com.securityservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class VerifyResponse {

    private String email;
    private boolean verified;

}
