package com.velatech.java_interview_assignment.exception;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException(){}

    public InvalidInputException(String message){
        super(message);
    }
}
