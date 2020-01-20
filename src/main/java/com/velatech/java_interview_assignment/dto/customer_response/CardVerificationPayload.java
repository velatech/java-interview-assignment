package com.velatech.java_interview_assignment.dto.customer_response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CardVerificationPayload {
    private String scheme;
    private String type;
    private String bank;

}
