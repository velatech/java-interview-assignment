package com.velatech.java_interview_assignment.exception;

public class InvalidInputExceptionHandler extends RuntimeException {
    public InvalidInputExceptionHandler(){}

    public InvalidInputExceptionHandler(String message){
        super(message);
    }
}
