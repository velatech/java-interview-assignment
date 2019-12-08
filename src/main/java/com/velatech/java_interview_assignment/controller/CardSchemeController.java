package com.velatech.java_interview_assignment.controller;

import com.velatech.java_interview_assignment.dto.customer_response.CardVerificationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("card-scheme")
public class CardSchemeController {

    @GetMapping("/verify/{cardNumber}")
    public ResponseEntity<CardVerificationResponse> verifyCard(@PathVariable String cardNumber){

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
