package com.velatech.java_interview_assignment.dto.binlist_api_response;

import lombok.Data;

@Data
public class Bank {
    private String name;
    private String url;
    private String phone;
    private String city;

    public Bank() {
    }
}
