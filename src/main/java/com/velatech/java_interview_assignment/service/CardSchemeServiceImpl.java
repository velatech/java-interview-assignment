package com.velatech.java_interview_assignment.service;

import com.velatech.java_interview_assignment.dto.binlist_api_response.BinListApiResponse;
import com.velatech.java_interview_assignment.dto.customer_response.CardStatisticsResponse;
import com.velatech.java_interview_assignment.dto.customer_response.CardVerificationPayload;
import com.velatech.java_interview_assignment.dto.customer_response.CardVerificationResponse;
import com.velatech.java_interview_assignment.exception.InvalidInputException;
import com.velatech.java_interview_assignment.exception.InvalidPageException;
import com.velatech.java_interview_assignment.model.CardDetail;
import com.velatech.java_interview_assignment.model.CardVerificationRecord;
import com.velatech.java_interview_assignment.repository.CardDetailRepository;
import com.velatech.java_interview_assignment.repository.CardVerificationRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.data.domain.Pageable;


@Service
public class CardSchemeServiceImpl implements CardSchemeService {
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
     * @param cardNumber:String
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

    /**
     * This function maps the BinList Api Response to the response structure of the client side
     * @param binListApiResponse: an object instantiated with the BinListApiResponse class
     * @return the mapped response
     */
    private CardVerificationResponse mapToCardVerificationResponse (BinListApiResponse binListApiResponse){
        CardVerificationResponse cardVerificationResponse = new CardVerificationResponse();
        CardVerificationPayload cardVerificationPayload = new CardVerificationPayload();

        if(binListApiResponse != null){
            cardVerificationResponse
                    .getPayload()
                    .setBank(binListApiResponse.getBank() == null ? "" : binListApiResponse.getBank().getName());

            cardVerificationResponse
                    .getPayload()
                    .setScheme(binListApiResponse.getScheme() == null ? "" : binListApiResponse.getScheme());

            cardVerificationResponse
                    .getPayload()
                    .setType(binListApiResponse.getType() == null ? "" : binListApiResponse.getType());

            cardVerificationResponse.setSuccess(true);
        }
        return cardVerificationResponse;
    }


    /**
     * This function maps the response from the database to the response structure of the client side
     * @param cardDetail: an object instantiated with the CardDetail class
     * @return the mapped response
     */
    private CardVerificationResponse mapToCardVerificationResponse ( CardDetail cardDetail){
        CardVerificationResponse cardVerificationResponse = new CardVerificationResponse();
        CardVerificationPayload cardVerificationPayload = new CardVerificationPayload();

        if(cardDetail != null){
            cardVerificationResponse
                    .getPayload()
                    .setBank(cardDetail.getBank() == null ? "" : cardDetail.getBank());

            cardVerificationResponse
                    .getPayload()
                    .setScheme(cardDetail.getScheme() == null ? "" : cardDetail.getScheme());

            cardVerificationResponse
                    .getPayload()
                    .setType(cardDetail.getBrand() == null ? "" : cardDetail.getBrand());

            cardVerificationResponse.setSuccess(true);
        }
        return cardVerificationResponse;
    }

    /**
     * This function maps the response from the BinList API to the structure of the database
     * @param cardNumber:String
     * @param binListApiResponse: an object instantiated with the BinListApiResponse class
     * @return the new generated object
     */
    private CardDetail mapToCardDetail(String cardNumber, BinListApiResponse binListApiResponse){

        CardDetail cardDetail = new CardDetail();
        cardDetail.setCardNumber(cardNumber);
        cardDetail.setCardNumberLength(binListApiResponse.getNumber() == null ? 0 : binListApiResponse.getNumber().getLength());
        cardDetail.setBank(binListApiResponse.getBank() == null ? "" : binListApiResponse.getBank().getName());
        cardDetail.setBrand(binListApiResponse.getBrand() == null ? "" : binListApiResponse.getBrand());
        cardDetail.setScheme(binListApiResponse.getScheme() == null ? "" : binListApiResponse.getScheme());
        cardDetail.setCountry(binListApiResponse.getCountry() == null ? "" : binListApiResponse.getCountry().getName());

        return cardDetail;
    }

    /**
     * This function saves card request details to the database
     * @param cardNumber:String
     * @param binListApiResponse: the response received from the Bin List API
     */
    @Async
    @CacheEvict(value = "cache", allEntries = true)
    public void saveRequestReturnObject(String cardNumber, BinListApiResponse binListApiResponse){
        CardDetail cardDetail = mapToCardDetail(cardNumber, binListApiResponse);
        cardDetailRepository.save(cardDetail);
    }

    /**
     * This function performs the actual verification of the card by making calls to the external API and returning appropriate responses
     * @param cardNumber:String
     * @param httpEntity
     * @return the converted response after receiving response from the external API
     * @throws HttpStatusCodeException
     * @throws InvalidInputException
     */
    @Override
    public CardVerificationResponse performCardVerification(String cardNumber, HttpEntity<?> httpEntity) throws HttpStatusCodeException, InvalidInputException {
        String validCardNumber = validateCardNumberLength(cardNumber);

        logCardVerificationRecord(validCardNumber);

        Optional<CardDetail> savedResponse = cardDetailRepository.findByCardNumber(validCardNumber);

        if(savedResponse.isPresent()){
            log.info("Response had been previously saved");
            return mapToCardVerificationResponse(savedResponse.get());
        }

        BinListApiResponse binListApiResponse = null;
        ResponseEntity<BinListApiResponse> response = null;

        try {
            response = restTemplate.exchange( binlistURL + "/{validCard}",
                    HttpMethod.GET, httpEntity, BinListApiResponse.class, validCardNumber);
            binListApiResponse = response.getBody();
        } catch (HttpStatusCodeException ex){
            log.error("Error performing request to BinList API");
            throw ex;
        }

        saveRequestReturnObject(validCardNumber, binListApiResponse);
        return mapToCardVerificationResponse(binListApiResponse);
    }


    @Override
    @Cacheable(value = "cache", key = "#root.method.name+ '_' +#pageable.pageNumber")
    public CardStatisticsResponse getCardVerificationRecords(Pageable pageable) throws RuntimeException {
        CardStatisticsResponse cardStatisticsResponse = new CardStatisticsResponse();

        Page<Map<String, Object>> page = cardVerificationRecordRepository
                .getCardVerificationRecordByCardNumber(pageable);

        if(page==null){
            log.error("Invalid Page Exception");
            throw  new InvalidPageException();
        }

        if(page.getSize() < 1L){
            log.error("General Runtime Exception");
            throw new RuntimeException();
        }

        cardStatisticsResponse.setSize(page.getNumber());
        cardStatisticsResponse.setLimit(page.getSize());
        cardStatisticsResponse.setSize(page.getTotalElements());
        if(page.hasContent()){
            cardStatisticsResponse.setSuccess(true);
            Map<String, Object> payload = new ConcurrentHashMap<>();
            for(Map<String, Object> item : page){
                payload.put(String.valueOf(item.get("cardNumber")), String.valueOf(item.get("count")));
            }
            cardStatisticsResponse.setPayload(payload);
        } else {
            log.warn("No content found for the page");
        }
        return cardStatisticsResponse;
    }

}

