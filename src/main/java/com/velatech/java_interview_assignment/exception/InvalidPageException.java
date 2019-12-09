package com.velatech.java_interview_assignment.exception;

public class InvalidPageException extends RuntimeException {
    public InvalidPageException(){}

    public InvalidPageException(String message){
        super(message);
    }
}
