package com.phayo.interviewentry.service;

import com.phayo.interviewentry.dto.client_response.CardHitsResponseDto;
import com.phayo.interviewentry.dto.client_response.CardVerifyResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

@Service
public interface CardService {
    CardVerifyResponseDto executeCardVerification(String cardNumber, HttpEntity<?> entity) throws HttpStatusCodeException;

    CardHitsResponseDto getCardVerificationRequestRecords(Pageable pageable) throws  RuntimeException;
}
