package com.velatech.java_interview_assignment.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "card_verification_records")
public class CardVerificationRecord {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    private String cardNumber;

    @Column(name="request_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date requestDate;

    public CardVerificationRecord(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
