package com.phayo.interviewentry.util;

public enum CardTypeEnum {
    CREDIT("credit"), DEBIT("debit");

    private String value;

    CardTypeEnum(String value) { this.setValue(value); }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
