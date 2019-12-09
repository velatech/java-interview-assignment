package com.velatech.java_interview_assignment.controller;

import com.velatech.java_interview_assignment.dto.customer_response.CardStatisticsResponse;
import com.velatech.java_interview_assignment.dto.customer_response.CardVerificationResponse;
import com.velatech.java_interview_assignment.exception.InvalidPageException;
import com.velatech.java_interview_assignment.service.CardSchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card-scheme")
public class CardSchemeController {

    @Autowired
    CardSchemeService cardSchemeService;

    @GetMapping("/verify/{cardNumber}")
    public ResponseEntity<CardVerificationResponse> verifyCard(@PathVariable String cardNumber){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        return new ResponseEntity<>(cardSchemeService.performCardVerification(cardNumber, entity), HttpStatus.OK);
    }

}
