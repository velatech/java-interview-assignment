package com.phayo.interviewentry.dto.external_api_response;

import lombok.Data;

@Data
public class BinListResponse {
    private Number number;

    private String scheme;

    private String type;

    private String brand;

    private boolean prepaid;

    private Country country;

    private Bank bank;

    public BinListResponse(){
    }
}

