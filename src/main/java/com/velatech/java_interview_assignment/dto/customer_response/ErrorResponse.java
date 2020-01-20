package com.velatech.java_interview_assignment.dto.customer_response;

import lombok.Data;

@Data
public class ErrorResponse {
    private String error;
    private String message;

}
