package com.inbank.martin_aarne_credit_decision_engine.web;

import com.inbank.martin_aarne_credit_decision_engine.interfaces.ICreditDecisionService;
import com.inbank.martin_aarne_credit_decision_engine.models.CreditDecisionItem;
import com.inbank.martin_aarne_credit_decision_engine.models.CreditDecisionResponse;
import com.inbank.martin_aarne_credit_decision_engine.models.CreditRequest;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = {CreditApplication.class})
@TestPropertySource("classpath:test.properties")
@RunWith(SpringRunner.class)
class CreditApplicationTests {
    @Autowired
    private ICreditDecisionService service;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private static String ssnDebt = "49002010965";
    private static String ssnCreditLow = "49002010976";
    private static String ssnCreditMed = "49002010987";
    private static String ssnCreditHigh = "49002010998";

    private double requestAmount = 5000;

    private static double minRequestAmount = 2000;
    private static double maxRequestAmount = 10000;

    private static int minPeriodInMonths = 12;
    private static int maxPeriodInMonths = 60;

    @Test
    void processCreditRequest_debt_notApproved() {
        CreditRequest request = mockRequest(ssnDebt, requestAmount, 12);

        CreditDecisionResponse response = service.processCreditRequest(request);

        assertTrue(response.isValid());

        assertNull(response.getCreditDecisionForRequestedPeriod());
    }

    @Test
    void processCreditRequest_highCredit_tooLittleRequested_minRequestAmountApproved() {
        requestAmount = minRequestAmount - 500;
        CreditRequest request = mockRequest(ssnCreditHigh, requestAmount, 12);

        CreditDecisionResponse response = service.processCreditRequest(request);

        CreditDecisionItem approvedRequest = response.getCreditDecisionForRequestedPeriod();

        assertTrue(response.isValid());

        assertNotNull(approvedRequest);
        assertTrue(approvedRequest.amount > requestAmount && approvedRequest.amount >= minRequestAmount);
    }

    @Test
    void processCreditRequest_highCredit_tooMuchRequested_maxAmountApproved() {
        requestAmount = maxRequestAmount + 20000;
        CreditRequest request = mockRequest(ssnCreditHigh, requestAmount, 12);

        CreditDecisionResponse response = service.processCreditRequest(request);

        assertTrue(response.isValid());

        CreditDecisionItem approvedRequest = response.getCreditDecisionForRequestedPeriod();
        assertNotNull(approvedRequest);
        assertTrue(approvedRequest.amount < requestAmount);
        assertEquals(maxRequestAmount, approvedRequest.amount);
    }

    @Test
    void processCreditRequest_highCredit_moreApprovedThanRequested() {
        requestAmount = 3000;
        CreditRequest request = mockRequest(ssnCreditHigh, requestAmount, 12);

        CreditDecisionResponse response = service.processCreditRequest(request);

        assertTrue(response.isValid());

        CreditDecisionItem approvedRequest = response.getCreditDecisionForRequestedPeriod();
        assertNotNull(approvedRequest);
        assertTrue(approvedRequest.amount >= requestAmount);
    }

    @Test
    void processCreditRequest_lowCredit_approvedForLongerPeriodAndLowerAmount() {
        requestAmount = 4000;
        int requestedPeriod = 12;
        CreditRequest request = mockRequest(ssnCreditMed, requestAmount, requestedPeriod);

        CreditDecisionResponse response = service.processCreditRequest(request);

        assertTrue(response.isValid());

        CreditDecisionItem approvedRequestWithLowerCredit = response.getCreditDecisionForRequestedPeriod();
        assertNotNull(approvedRequestWithLowerCredit);
        assertTrue(approvedRequestWithLowerCredit.amount < requestAmount);


        CreditDecisionItem approvedRequestWithRequestedCredit = response.getApprovedCreditDecisionWithMinPeriod();
        assertNotNull(approvedRequestWithRequestedCredit);
        assertTrue(approvedRequestWithRequestedCredit.amount == requestAmount);
        assertTrue(approvedRequestWithRequestedCredit.periodInMonths > requestedPeriod);
    }

    @Test
    void processCreditRequest_invalidAmount_notValid() {
        requestAmount = -9000;
        CreditRequest request = mockRequest(ssnCreditHigh, requestAmount, 12);

        CreditDecisionResponse response = service.processCreditRequest(request);
        assertFalse(response.isValid());
    }

    @Test
    void processCreditRequest_nullRequest_notValid() {
        CreditDecisionResponse response = service.processCreditRequest(null);
        assertFalse(response.isValid());
    }

    @Test
    void processCreditRequest_invalidPeriod_notValid() {
        CreditRequest request = mockRequest(ssnCreditHigh, requestAmount, -5);
        CreditDecisionResponse response = service.processCreditRequest(request);

        assertFalse(response.isValid());
    }

    @Test
    void processCreditRequest_invalidSsn_notApproved() {
        String ssn = "testSsnNotValidAtAll";
        CreditRequest request = mockRequest(ssn, requestAmount, 12);

        CreditDecisionResponse response = service.processCreditRequest(request);

        assertFalse(response.isValid());
        assertNull(response.getCreditDecisionForRequestedPeriod());
        assertNull(response.getApprovedCreditDecisionWithMinPeriod());
    }

    private static CreditRequest mockRequest(String ssn, double amount, int periodMonths) {
        CreditRequest creditRequest = new CreditRequest();

        creditRequest.setSsn(ssn);
        creditRequest.setCreditAmount(amount);
        creditRequest.setPeriodInMonths(periodMonths);

        return creditRequest;
    }

}
