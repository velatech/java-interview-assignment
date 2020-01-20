package com.velatech.java_interview_assignment.service;

import com.velatech.java_interview_assignment.dto.customer_response.CardStatisticsResponse;
import com.velatech.java_interview_assignment.dto.customer_response.CardVerificationResponse;
import com.velatech.java_interview_assignment.exception.InvalidInputException;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.data.domain.Pageable;

@Service
public interface CardSchemeService {
    CardVerificationResponse performCardVerification(String cardNumber, HttpEntity<?> entity) throws HttpStatusCodeException, InvalidInputException;

    CardStatisticsResponse getCardVerificationRecords(Pageable pageable) throws RuntimeException;
}
