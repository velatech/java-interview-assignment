package com.velatech.java_interview_assignment.dto.customer_response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CardVerificationResponse {
    private boolean success;
    private CardVerificationPayload payload;
}
