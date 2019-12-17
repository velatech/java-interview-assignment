package com.phayo.interviewentry.dto.client_response;

import lombok.Data;

@Data
public class CardVerifyPayloadDto {
    private String scheme;
    private String type;
    private String bank;

    public CardVerifyPayloadDto() {
    }
}
