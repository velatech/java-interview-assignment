package com.phayo.interviewentry.exception;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException(){}

    public InvalidInputException(String message){ super(message); }
}
