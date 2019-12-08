package com.phayo.interviewentry.service;

import com.phayo.interviewentry.dto.client_response.CardHitsResponseDto;
import com.phayo.interviewentry.dto.client_response.CardVerifyPayloadDto;
import com.phayo.interviewentry.dto.client_response.CardVerifyResponseDto;
import com.phayo.interviewentry.dto.external_api_response.BinListResponse;
import com.phayo.interviewentry.exception.InvalidInputException;
import com.phayo.interviewentry.exception.InvalidPageException;
import com.phayo.interviewentry.model.CardDetailEntity;
import com.phayo.interviewentry.model.CardVerificationRequestRecord;
import com.phayo.interviewentry.repository.CardDetailEntityRepository;
import com.phayo.interviewentry.repository.CardVerificationRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

@Service
public class CardServiceImpl implements CardService {

    private Logger log = LoggerFactory.getLogger(CardServiceImpl.class);

    @Value("${binlist.url}")
    String binlistURL;

    @Autowired
    CardVerificationRequestRepository verificationRequestRepository;

    @Autowired
    CardDetailEntityRepository cardDetailEntityRepository;

    @Autowired
    RestTemplate restTemplate;

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

    /**
     * Transforms the BinList rsponse to the CardVerifyResponseDto which meets our clients expectation
     * @param binListResponse Object of BinListResponse gotten from Third party API
     * @return CardVerifyResponseDto
     */
    private CardVerifyResponseDto mapToCardVerifyResponseDto(BinListResponse binListResponse){
        CardVerifyResponseDto cardVerifyResponseDto = new CardVerifyResponseDto();
        CardVerifyPayloadDto cardVerifyPayloadDto = new CardVerifyPayloadDto();

        if(binListResponse != null){
            cardVerifyResponseDto.setPayload(cardVerifyPayloadDto);
            cardVerifyResponseDto
                    .getPayload()
                    .setBank(binListResponse.getBank() == null ? "" : binListResponse.getBank().getName());

            cardVerifyResponseDto
                    .getPayload()
                    .setScheme(binListResponse.getScheme() == null ? "" : binListResponse.getScheme());

            cardVerifyResponseDto
                    .getPayload()
                    .setType(binListResponse.getType() == null ? "" : binListResponse.getType());

            cardVerifyResponseDto.setSuccess(true);

        }

        return cardVerifyResponseDto;
    }

    /**
     * Transforms BinListResponse object to an object of type CardDetailEntity
     * @param cardNumber Validated card Number
     * @param binListResponse Response from third-party API
     * @return CardDetailEntity
     */
    private CardDetailEntity mapToCardDetailEntity(String cardNumber, BinListResponse binListResponse){
//        Bank bank = new Bank();
//        bank.setCity(binListResponse.getBank() == null ? "" : binListResponse.getBank().getCity());
//        bank.setName(binListResponse.getBank() == null ? "" : binListResponse.getBank().getName());
//        bank.setPhone(binListResponse.getBank() == null ? "" : binListResponse.getBank().getPhone());
//        bank.setUrl(binListResponse.getBank() == null ? "" : binListResponse.getBank().getUrl());
//
//        Brand brand = new Brand(binListResponse.getBrand() == null ? "" : binListResponse.getBrand());
//        CardScheme scheme = new CardScheme(binListResponse.getScheme() == null ? "" : binListResponse.getScheme());
//
//        Country country = new Country();
//        country.setCurrency(binListResponse.getCountry() == null ? "" : binListResponse.getCountry().getCurrency());
//        country.setName(binListResponse.getCountry() == null ? "" : binListResponse.getCountry().getName());

        CardDetailEntity cardDetailEntity = new CardDetailEntity();
        cardDetailEntity.setCardNumber(cardNumber);
        cardDetailEntity.setCardNumberLength(binListResponse.getNumber() == null ? 0 : binListResponse.getNumber().getLength());
        cardDetailEntity.setBank(binListResponse.getBank() == null ? "" : binListResponse.getBank().getName());
        cardDetailEntity.setBrand(binListResponse.getBrand() == null ? "" : binListResponse.getBrand());
        cardDetailEntity.setScheme(binListResponse.getScheme() == null ? "" : binListResponse.getScheme());
        cardDetailEntity.setCountry(binListResponse.getCountry() == null ? "" : binListResponse.getCountry().getName());

        return cardDetailEntity;
    }

    /**
     * Saves the Transform BinList response to the database
     * @param cardNumber validated card number
     * @param binListResponse Response from third-party API
     */
    @Async
    @CacheEvict(value = "logCache", allEntries = true)
    public void saveRequestReturnObject(String cardNumber, BinListResponse binListResponse){
        CardDetailEntity cardDetailEntity = mapToCardDetailEntity(cardNumber, binListResponse);
        cardDetailEntityRepository.save(cardDetailEntity);
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

        try {
            response = restTemplate.exchange( binlistURL + "/{validCardNumber}",
                    HttpMethod.GET, entity, BinListResponse.class, validCardNumber);
            binListResponse = response.getBody();
        } catch (HttpStatusCodeException exception){
            log.error("Error executing request to lookup.binlist.net");
            throw  exception;
        }

        // Saves the binlist response to database
        saveRequestReturnObject(validCardNumber, binListResponse);

        return mapToCardVerifyResponseDto(binListResponse);
    }

    @Override
    @Cacheable(value = "logCache", key = "#root.method.name+ '_' +#pageable.pageNumber")
    public CardHitsResponseDto getCardVerificationRequestRecords(Pageable pageable) throws RuntimeException {
        CardHitsResponseDto cardHitsResponseDto = new CardHitsResponseDto();

        Page<Map<String, Object>> page = verificationRequestRepository
                .getVerificationRequestRecordGroupedByCardNumber(pageable);

        if(page == null){
            log.error("Invalid Page Exception Thrown");
            throw new InvalidPageException();
        }

        if(page.getSize() < 1L){
            log.error("General Runtime Exception Thrown");
            throw new RuntimeException();
        }

        log.info("Pageable response checks out");
        cardHitsResponseDto.setSize(page.getNumber());
        cardHitsResponseDto.setLimit(page.getSize());
        cardHitsResponseDto.setSize(page.getTotalElements());
        if(page.hasContent()){
            cardHitsResponseDto.setSuccess(true);
            Map<String, Object> payload = new ConcurrentHashMap<>();
            for(Map<String, Object> item : page){
                payload.put(String.valueOf(item.get("cardNumber")), String.valueOf(item.get("count")));
            }
            cardHitsResponseDto.setPayload(payload);
        } else {
            log.warn("page has no content");
        }

        return cardHitsResponseDto;
    }
}
