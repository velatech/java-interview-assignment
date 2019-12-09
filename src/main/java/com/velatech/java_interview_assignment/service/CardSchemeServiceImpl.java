package com.velatech.java_interview_assignment.service;

import com.velatech.java_interview_assignment.exception.InvalidInputException;
import com.velatech.java_interview_assignment.model.CardVerificationRecord;
import com.velatech.java_interview_assignment.repository.CardDetailRepository;
import com.velatech.java_interview_assignment.repository.CardVerificationRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class CardSchemeServiceImpl {
    @Value("${binlist.url}")
    String binlistURL;

    private Logger log = LoggerFactory.getLogger(CardSchemeServiceImpl.class);

    @Autowired
    CardVerificationRecordRepository cardVerificationRecordRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CardDetailRepository cardDetailRepository;

    /**
     * This function logs a new verification request to the database
     * @param cardNumber:String
     */
    private void logCardVerificationRecord(String cardNumber) {
        log.info("Logged a new verification request to the database");
        cardVerificationRecordRepository.save(new CardVerificationRecord(cardNumber));
    }

    /**
     * This function validates the length of the card
     * @param cardNumber
     * @return the card number (truncated to 6 digits) if the input falls within the range of 6 to 19 characters
     * @throws InvalidInputException if the input is invalid
     */
    private String validateCardNumberLength(String cardNumber) throws InvalidInputException{

        if ( !cardNumber.matches("\\d{6,19}")){
            throw new InvalidInputException("Invalid Card Number Input");
        }

        log.info("Card Validated");
        if (cardNumber.length() > 6){
            return cardNumber.substring(0, 6);
        }

        return cardNumber;
    }



}
