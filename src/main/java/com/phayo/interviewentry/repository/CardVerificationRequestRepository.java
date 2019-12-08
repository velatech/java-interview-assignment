package com.phayo.interviewentry.repository;

import com.phayo.interviewentry.model.CardVerificationRequestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardVerificationRequestRepository extends JpaRepository<CardVerificationRequestRecord, Long> {

}
