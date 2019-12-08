package com.phayo.interviewentry.service;

import com.phayo.interviewentry.dto.client_response.CardVerifyPayloadDto;
import com.phayo.interviewentry.dto.client_response.CardVerifyResponseDto;
import com.phayo.interviewentry.dto.external_api_response.BinListResponse;
import com.phayo.interviewentry.exception.InvalidInputException;
import com.phayo.interviewentry.model.CardDetailEntity;
import com.phayo.interviewentry.model.CardVerificationRequestRecord;
import com.phayo.interviewentry.repository.CardDetailEntityRepository;
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

    @Autowired
    CardDetailEntityRepository cardDetailEntityRepository;

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

    /**
     * Transforms the BinList rsponse to the CardVerifyResponseDto which meets our clients expectation
     * @param cardDetailEntity Object of CardDetail stored on the Database
     * @return CardVerifyResponseDto
     */
    private CardVerifyResponseDto mapToCardVerifyResponseDto(CardDetailEntity cardDetailEntity){
        CardVerifyResponseDto cardVerifyResponseDto = new CardVerifyResponseDto();
        CardVerifyPayloadDto cardVerifyPayloadDto = new CardVerifyPayloadDto();

        if(cardDetailEntity != null){
            cardVerifyResponseDto.setPayload(cardVerifyPayloadDto);

            cardVerifyResponseDto.getPayload()
                    .setBank(cardDetailEntity.getBank() == null ? "" : cardDetailEntity.getBank());
            cardVerifyResponseDto.getPayload()
                    .setScheme(cardDetailEntity.getScheme() == null ? "" : cardDetailEntity.getScheme());
            cardVerifyResponseDto.getPayload()
                    .setType(cardDetailEntity.getBrand() == null ? "" : cardDetailEntity.getBrand());
        }

        return cardVerifyResponseDto;
    }

    @Override
    public CardVerifyResponseDto executeCardVerification(String cardNumber, HttpEntity<?> entity) throws HttpStatusCodeException, InvalidInputException {
        // Validate the card number in the request
        String validCardNumber = validateCardNumber(cardNumber);

        // Log the request to database
        logCardVerificationRequest(validCardNumber);

        BinListResponse binListResponse = null;
        ResponseEntity<BinListResponse> response = null;

        // Checks if a request has been previously made to the external API and return the saved response
        Optional<CardDetailEntity> savedResponse = cardDetailEntityRepository.findByCardNumber(validCardNumber);
        if (savedResponse.isPresent()){
            log.info("There was a saved response");
            return mapToCardVerifyResponseDto(savedResponse.get());
        }
    }
}
