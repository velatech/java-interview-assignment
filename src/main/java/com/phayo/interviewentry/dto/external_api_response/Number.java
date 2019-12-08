package com.phayo.interviewentry.dto.external_api_response;

import lombok.Data;

@Data
public class Number {
    private int length;
    private boolean luhn;

    public Number() {
    }
}
