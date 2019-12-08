package com.phayo.interviewentry.service;

import com.phayo.interviewentry.dto.client_response.CardVerifyResponseDto;
import com.phayo.interviewentry.exception.InvalidInputException;
import com.phayo.interviewentry.model.CardVerificationRequestRecord;
import com.phayo.interviewentry.repository.CardVerificationRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Optional;

public class CardServiceImpl implements CardService {

    private Logger log = LoggerFactory.getLogger(CardServiceImpl.class);

    @Autowired
    CardVerificationRequestRepository verificationRequestRepository;

    /**
     * @Params: cardNumber:String
     * @Returns: cardNumber:String
     * Checks if the card number is a digit between 6 and 19 of length.
     * it throws an @InvalidInputException if the criteria fails and returns
     * a truncated 6 digit String otherwise.
     */
    private String validateCardNumber(String cardNumber) throws InvalidInputException{

        if ( !cardNumber.matches("\\d{6,19}")){
            throw new InvalidInputException("Card Number Input is not valid");
        }

        log.info("Card Validated");
        if (cardNumber.length() > 6){
            return cardNumber.substring(0, 6);
        }

        return cardNumber;
    }

    private void logCardVerificationRequest(String validCardNumber) {
        log.info("Saved a card verification request to database");
        verificationRequestRepository.save(new CardVerificationRequestRecord(validCardNumber));
    }

    @Override
    public CardVerifyResponseDto executeCardVerification(String cardNumber, HttpEntity<?> entity) throws HttpStatusCodeException, InvalidInputException {
        // Validate the card number in the request
        String validCardNumber = validateCardNumber(cardNumber);

        logCardVerificationRequest(validCardNumber);
    }
}
