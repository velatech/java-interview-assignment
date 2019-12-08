package com.phayo.interviewentry.exception;

import com.phayo.interviewentry.dto.client_response.CardVerifyResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = {InvalidInputException.class, InvalidPageException.class
    })
    public ResponseEntity<?> invalidInputException(InvalidInputException ex, WebRequest request){
        logger.error(ex.getMessage());
        CardVerifyResponseDto responseDto = new CardVerifyResponseDto();
        responseDto.setSuccess(false);
        responseDto.setPayload(null);
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {org.springframework.web.client.HttpStatusCodeException.class})
    public ResponseEntity<?> statusCodeException(HttpStatusCodeException ex, WebRequest request){
        logger.error(ex.getMessage());
        CardVerifyResponseDto responseDto = new CardVerifyResponseDto();
        responseDto.setSuccess(false);
        responseDto.setPayload(null);
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        logger.error(ex.getMessage());
        CardVerifyResponseDto responseDto = new CardVerifyResponseDto();
        responseDto.setSuccess(false);
        responseDto.setPayload(null);
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
