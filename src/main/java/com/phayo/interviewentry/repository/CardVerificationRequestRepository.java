package com.phayo.interviewentry.repository;

import com.phayo.interviewentry.model.CardVerificationRequestRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface CardVerificationRequestRepository extends JpaRepository<CardVerificationRequestRecord, Long> {
    @Query("SELECT record.cardNumber AS cardNumber, COUNT(record.cardNumber) AS count FROM CardVerificationRequestRecord record GROUP BY record.cardNumber")
    Page<Map<String, Object>> getVerificationRequestRecordGroupedByCardNumber(Pageable pageable);
}
