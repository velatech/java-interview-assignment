package com.velatech.java_interview_assignment.exception;

import com.velatech.java_interview_assignment.dto.customer_response.CardVerificationResponse;
import com.velatech.java_interview_assignment.dto.customer_response.ErrorResponse;
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

    @ExceptionHandler(value = {InvalidInputException.class})
    public ResponseEntity<?> invalidInputException(InvalidInputException ex, WebRequest request){
        logger.error(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(HttpStatus.BAD_REQUEST.toString());
        errorResponse.setMessage("Invalid Input ... Please try again");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InvalidPageException.class})
    public ResponseEntity<?> invalidPageException(InvalidPageException ex, WebRequest request){
        logger.error(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(HttpStatus.BAD_REQUEST.toString());
        errorResponse.setMessage("Invalid Input ... Please try again");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {org.springframework.web.client.HttpStatusCodeException.class})
    public ResponseEntity<?> statusCodeException(HttpStatusCodeException ex, WebRequest request){
        logger.error(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        errorResponse.setMessage("Sorry ... It's not your fault ... It's from us");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        logger.error(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        errorResponse.setMessage("Sorry ... It's not your fault ... It's from us");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
