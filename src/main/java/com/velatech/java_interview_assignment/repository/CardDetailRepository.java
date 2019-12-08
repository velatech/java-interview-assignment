package com.velatech.java_interview_assignment.repository;

import com.velatech.java_interview_assignment.model.CardDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardDetailRepository extends JpaRepository<CardDetail, String> {
    Optional<CardDetail> findByCardNumber(String cardNumber);
}
