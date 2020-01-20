package com.velatech.java_interview_assignment.dto.binlist_api_response;

import lombok.Data;

@Data
public class Number {
    private int length;
    private boolean luhn;

    public Number() {
    }
}
