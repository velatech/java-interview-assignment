package com.phayo.interviewentry.dto.external_api_response;

import lombok.Data;

@Data
public class Country {
    private String numeric;

    private String alpha2;

    private String name;

    private String emoji;

    private String currency;

    private long latitude;

    private long longitude;

    public Country() {
    }
}
