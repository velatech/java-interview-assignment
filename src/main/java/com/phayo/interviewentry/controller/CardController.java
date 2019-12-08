package com.phayo.interviewentry.controller;

import com.phayo.interviewentry.dto.client_response.CardHitsResponseDto;
import com.phayo.interviewentry.dto.client_response.CardVerifyResponseDto;
import com.phayo.interviewentry.exception.InvalidPageException;
import com.phayo.interviewentry.service.CardService;
import com.phayo.interviewentry.service.CardServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card-scheme")
public class CardController {
    @Autowired
    CardService cardService;

    private Logger log = LoggerFactory.getLogger(CardServiceImpl.class);

    @GetMapping("/verify/{cardNumber}")
    public ResponseEntity<CardVerifyResponseDto> verifyCard(@PathVariable String cardNumber){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        return new ResponseEntity<>(cardService.executeCardVerification(cardNumber, entity), HttpStatus.OK);
    }

    @GetMapping(value = "/stats", params = { "start", "limit" })
    public ResponseEntity<CardHitsResponseDto> getCardHits(@RequestParam("start") int start,
                                                           @RequestParam("limit") int limit){
        if(start < 0) throw new InvalidPageException();
        if(start >= 1) start--;
        log.info(String.valueOf(start));
        return new ResponseEntity<>(cardService.getCardVerificationRequestRecords(PageRequest.of(start, limit)), HttpStatus.OK);
    }
}
