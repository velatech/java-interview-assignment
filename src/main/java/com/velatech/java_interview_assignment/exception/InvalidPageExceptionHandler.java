package com.velatech.java_interview_assignment.exception;

public class InvalidPageExceptionHandler extends RuntimeException {
    public InvalidPageExceptionHandler(){}

    public InvalidPageExceptionHandler(String message){
        super(message);
    }
}
