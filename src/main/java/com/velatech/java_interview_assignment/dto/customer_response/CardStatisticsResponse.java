package com.velatech.java_interview_assignment.dto.customer_response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class CardStatisticsResponse {
    private boolean success;
    private int start;
    private int limit;
    private long size;
    private Map<String, Object> payload;
}
