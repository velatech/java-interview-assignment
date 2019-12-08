package com.phayo.interviewentry.dto.external_api_response;

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
