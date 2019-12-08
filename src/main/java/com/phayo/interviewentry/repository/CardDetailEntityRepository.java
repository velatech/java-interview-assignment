package com.phayo.interviewentry.repository;

import com.phayo.interviewentry.model.CardDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardDetailEntityRepository extends JpaRepository<CardDetailEntity, String> {
    Optional<CardDetailEntity> findByCardNumber(String cardNumber);
}
