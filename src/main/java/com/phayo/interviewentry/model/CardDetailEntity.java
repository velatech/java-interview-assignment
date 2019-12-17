package com.phayo.interviewentry.model;

import com.phayo.interviewentry.util.CardTypeEnum;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class CardDetailEntity{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "card_number_length")
    private int cardNumberLength;

    @Column(name = "luhn_check")
    private boolean luhn;

    private String scheme;

    private String brand;

    @Enumerated(EnumType.STRING)
    private CardTypeEnum type;

    private String country;

    //    @ManyToOne(cascade = CascadeType.ALL)
    private String bank;
}

