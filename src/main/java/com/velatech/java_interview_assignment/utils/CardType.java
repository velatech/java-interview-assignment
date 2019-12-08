package com.velatech.java_interview_assignment.utils;

public enum CardType {

    CREDIT("credit"), DEBIT("debit");

    private String value;

    CardType(String value) { this.setValue(value); }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
