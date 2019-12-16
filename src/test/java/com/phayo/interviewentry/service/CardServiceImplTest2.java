package com.phayo.interviewentry.service;

import com.phayo.interviewentry.dto.client_response.CardVerifyResponseDto;
import com.phayo.interviewentry.dto.external_api_response.BinListResponse;
import com.phayo.interviewentry.exception.InvalidInputException;
import com.phayo.interviewentry.model.CardDetailEntity;
import com.phayo.interviewentry.model.CardVerificationRequestRecord;
import com.phayo.interviewentry.repository.CardDetailEntityRepository;
import com.phayo.interviewentry.repository.CardVerificationRequestRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class CardServiceImplTest2 {
    @Mock
    CardDetailEntityRepository cardDetailEntityRepository;

    @Mock
    CardVerificationRequestRepository cardVerificationRequestRepository;

    @Mock
    RestTemplate restTemplate;


    @InjectMocks
    CardServiceImpl cardService;



    @Test
    public void verify_Card_Call_External_Api_For_New_Card(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        Optional<CardDetailEntity> value = Optional.empty();

        BinListResponse response = new BinListResponse();

        response.setBrand("MasterCard");
        response.setScheme("Debit");
        response.setType("debit");

        ResponseEntity<BinListResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.ACCEPTED);

        when(cardDetailEntityRepository.findByCardNumber(ArgumentMatchers.anyString())).thenReturn(value);

        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.<HttpEntity<BinListResponse>>any(),
                ArgumentMatchers.<Class<BinListResponse>>any(),
                ArgumentMatchers.anyString()))
                .thenReturn(responseEntity);

        when(cardVerificationRequestRepository.save(ArgumentMatchers.any(CardVerificationRequestRecord.class)))
        .thenReturn(new CardVerificationRequestRecord("123456789"));

        CardVerifyResponseDto responseDto = cardService.executeCardVerification("123456789", entity);

        assertEquals(responseDto.getPayload().getType(), response.getType());
        assertTrue(responseDto.isSuccess());
    }

    @Test
    public void returns_saved_card_list_from_database(){
        CardDetailEntity response = new CardDetailEntity();

        response.setBank("Fidelity");
        response.setScheme("Credit");
        response.setBrand("Credit");

        Optional<CardDetailEntity> value = Optional.of(response);

        when(cardDetailEntityRepository.findByCardNumber(ArgumentMatchers.anyString())).thenReturn(value);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        CardVerifyResponseDto responseDto = cardService.executeCardVerification("123456789", entity);

        assertTrue(responseDto.isSuccess());

        assertEquals(responseDto.getPayload().getBank(), response.getBank());
        assertEquals(responseDto.getPayload().getType(), response.getBrand());
    }

    @Test
    public void throws_error_on_wrong_input(){
        String wrongCardNumber = "2345";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        assertThrows(InvalidInputException.class, () -> {
            cardService.executeCardVerification(wrongCardNumber, entity);
        });
    }

//    @Test
//    public void returns_a_paginated_object(){
//        Map<String, Integer> map = new HashMap<>();
//        map.put("123456", 1);
//        map.put("123457", 2);
//        map.put("123458", 1);
////        Page<?> paginatedPage = ;
//    }
}
