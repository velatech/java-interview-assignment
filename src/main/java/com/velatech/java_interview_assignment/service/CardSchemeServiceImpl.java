package com.velatech.java_interview_assignment.service;

import com.velatech.java_interview_assignment.repository.CardVerificationRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class CardSchemeServiceImpl {
    @Value("${binlist.url}")
    String binlistURL;

    private Logger log = LoggerFactory.getLogger(CardSchemeServiceImpl.class);

    @Autowired
    CardVerificationRecordRepository cardVerificationRecordRepository;


}
