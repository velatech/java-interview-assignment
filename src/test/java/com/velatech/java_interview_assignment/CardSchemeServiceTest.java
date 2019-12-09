package com.velatech.java_interview_assignment;

import com.velatech.java_interview_assignment.dto.customer_response.CardVerificationPayload;
import com.velatech.java_interview_assignment.dto.customer_response.CardVerificationResponse;
import com.velatech.java_interview_assignment.exception.InvalidInputException;
import com.velatech.java_interview_assignment.service.CardSchemeService;
import com.velatech.java_interview_assignment.service.CardSchemeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.junit.Rule;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

//@RunWith(SpringJUnit4ClassRunner.class) // @RunWith: integrate spring with junit
@SpringBootTest(classes = {JavaInterviewAssignmentApplication.class}) // @SpringBootTest: this class is spring boot test.
public class CardSchemeServiceTest {
    @Value("https://lookup.binlist.net")
    String binlistURL;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private CardSchemeServiceImpl cardSchemeService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    void performCardVerification_Returns_Expected_Result_For_A_Valid_Card() {
        String cardNumber = "5399832045691234";

        CardVerificationResponse expectedResponse = new CardVerificationResponse();
        expectedResponse.setSuccess(true);
        expectedResponse.setPayload(new CardVerificationPayload());
        expectedResponse.getPayload().setType("debit");
        expectedResponse.getPayload().setScheme("mastercard");
        expectedResponse.getPayload().setBank("GTBANK");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        CardVerificationResponse actualResponse = cardSchemeService.performCardVerification(cardNumber, entity);

        assertTrue(actualResponse.isSuccess());
        assertEquals(expectedResponse.getPayload().getBank(), actualResponse.getPayload().getBank());
        assertEquals(expectedResponse.getPayload().getScheme(), actualResponse.getPayload().getScheme());
        assertEquals(expectedResponse.getPayload().getType().toLowerCase(), actualResponse.getPayload().getType().toLowerCase());
    }

//    @Test
//    void performCardVerification_Returns_Expected_Result_For_An_Invalid_Card() {
//        String cardNumber = "5912334433329922";
//
//        CardVerificationResponse expectedResponse = new CardVerificationResponse();
//        expectedResponse.setSuccess(false);
//        expectedResponse.setPayload(null);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<?> entity = new HttpEntity<>(headers);
//
//        CardVerificationResponse actualResponse = cardSchemeService.performCardVerification(cardNumber, entity);
//
//        assertTrue(!actualResponse.isSuccess());
//        assertEquals(expectedResponse.getPayload(), actualResponse.getPayload());
//    }

    @Test
    void performCardVerification_Throws_Expected_Error(){
        String wrongCardNumber = "12345";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        assertThrows(InvalidInputException.class, () -> {
            cardSchemeService.performCardVerification(wrongCardNumber, entity);
        });
    }
}
