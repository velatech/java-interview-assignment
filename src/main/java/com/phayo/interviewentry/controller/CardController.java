package com.phayo.interviewentry.controller;

import com.phayo.interviewentry.dto.client_response.CardHitsResponseDto;
import com.phayo.interviewentry.dto.client_response.CardVerifyResponseDto;
import com.phayo.interviewentry.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card-scheme")
public class CardController {
    @Autowired
    CardService cardService;

    @GetMapping("/verify/{cardNumber}")
    public ResponseEntity<CardVerifyResponseDto> verifyCard(@PathVariable String cardNumber){

    }

    @GetMapping(value = "/stats", params = { "start", "limit" })
    public ResponseEntity<CardHitsResponseDto> getCardHits(@RequestParam("start") int start,
                                                           @RequestParam("limit") int limit){

    }
}
