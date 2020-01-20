package com.velatech.java_interview_assignment.model;

import com.velatech.java_interview_assignment.utils.CardType;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class CardDetail {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @Column(name = "card_number")
    private String cardNumber;

    private String scheme;

    @Enumerated(EnumType.STRING)
    private CardType type;

    private String bank;

}
