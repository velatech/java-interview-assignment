package com.phayo.interviewentry.service;

import com.phayo.interviewentry.InterviewEntryApplication;
import com.phayo.interviewentry.dto.client_response.CardVerifyPayloadDto;
import com.phayo.interviewentry.dto.client_response.CardVerifyResponseDto;
import com.phayo.interviewentry.exception.InvalidInputException;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import javax.annotation.Resource;



import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {InterviewEntryApplication.class})
class CardServiceImplTest {
    @Value("https://lookup.binlist.net")
    String binlistURL;

    @Resource
    private CardServiceImpl cardService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    void executeCardVerification_Returns_Expected_Result() {
        String cardNumber = "4187421939476631";

        CardVerifyResponseDto expectedResponse = new CardVerifyResponseDto();
        expectedResponse.setSuccess(true);
        expectedResponse.setPayload(new CardVerifyPayloadDto());
        expectedResponse.getPayload().setType("debit");
        expectedResponse.getPayload().setScheme("visa");
        expectedResponse.getPayload().setBank("ACCESS");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);


        CardVerifyResponseDto actualResponse = cardService.executeCardVerification(cardNumber, entity);

        assertTrue(actualResponse.isSuccess());
        assertEquals(expectedResponse.getPayload().getBank(), actualResponse.getPayload().getBank());
        assertEquals(expectedResponse.getPayload().getScheme(), actualResponse.getPayload().getScheme());
        assertEquals(expectedResponse.getPayload().getType().toLowerCase(), actualResponse.getPayload().getType().toLowerCase());

    }

    @Test
    void executeCardVerification_Throws_Expected_Error(){
        String wrongCardNumber = "2345";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        assertThrows(InvalidInputException.class, () -> {
            cardService.executeCardVerification(wrongCardNumber, entity);
        });
    }
}