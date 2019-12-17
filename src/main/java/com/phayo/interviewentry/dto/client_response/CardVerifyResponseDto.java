package com.phayo.interviewentry.dto.client_response;

import lombok.Data;

@Data
public class CardVerifyResponseDto {
    private boolean success;
    private CardVerifyPayloadDto payload;

    public CardVerifyResponseDto() {
    }
}
