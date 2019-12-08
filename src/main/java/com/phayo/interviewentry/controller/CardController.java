package com.phayo.interviewentry.controller;

import com.phayo.interviewentry.dto.client_response.CardHitsResponseDto;
import com.phayo.interviewentry.dto.client_response.CardVerifyResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card-scheme")
public class CardController {


    @GetMapping("/verify/{cardNumber}")
    public ResponseEntity<CardVerifyResponseDto> verifyCard(@PathVariable String cardNumber){

    }

    @GetMapping(value = "/stats", params = { "start", "limit" })
    public ResponseEntity<CardHitsResponseDto> getCardHits(@RequestParam("start") int start,
                                                           @RequestParam("limit") int limit){

    }
}
