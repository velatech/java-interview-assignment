package com.phayo.interviewentry.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "card_verification_request_logs")
public class CardVerificationRequestRecord {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    private String cardNumber;

    @Column(name="date_of_withdrawal", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date requestDate;

    public CardVerificationRequestRecord(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
