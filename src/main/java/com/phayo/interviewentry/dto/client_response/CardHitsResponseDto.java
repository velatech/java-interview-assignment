package com.phayo.interviewentry.dto.client_response;

import lombok.Data;

import java.util.Map;

@Data
public class CardHitsResponseDto {
    private boolean success;
    private int start;
    private int limit;
    private long size;
    private Map<String, Object> payload;

    public CardHitsResponseDto() {
    }
}
