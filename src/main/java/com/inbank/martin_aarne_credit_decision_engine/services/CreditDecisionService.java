package com.inbank.martin_aarne_credit_decision_engine.services;

import com.inbank.martin_aarne_credit_decision_engine.interfaces.ICreditDecisionService;
import com.inbank.martin_aarne_credit_decision_engine.interfaces.ICreditRegistry;
import com.inbank.martin_aarne_credit_decision_engine.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:application.properties")
public class CreditDecisionService implements ICreditDecisionService {
    private ICreditRegistry creditRegistry;

    @Value("${minPeriodInMonths}")
    private int minCreditPeriodInMonths;
    @Value("${maxPeriodInMonths}")
    private int maxCreditPeriodInMonths;
    @Value("${minCreditAmount}")
    private double minCreditAmount;
    @Value("${maxCreditAmount}")
    private double maxCreditAmount;

    @Autowired
    public CreditDecisionService(ICreditRegistry creditRegistry){
        this.creditRegistry = creditRegistry;
    }

    @Override
    public CreditDecisionResponse processCreditRequest(CreditRequest request) {
        CreditDecisionResponse response = new CreditDecisionResponse();
        response.setRequest(request);

        validateRequest(request, response);

        if(!response.isValid()){
            return response;
        }

        CreditModifierResponse modifier = creditRegistry.getCreditModifier(request.getSsn());

        if (modifier == null || modifier.getCreditModifier() <= 0) {
            return response;
        }

        CreditCalculationRequest calcRequest = new CreditCalculationRequest();
        calcRequest.setCreditAmount(getValidRequestedAmount(request.getCreditAmount()));
        calcRequest.setPeriodInMonths(getValidRequestPeriod(request.getPeriodInMonths()));
        calcRequest.setCreditModifier(modifier.getCreditModifier());

        processCreditRequest(calcRequest, response);

        return response;
    }

    private void validateRequest(CreditRequest request, CreditDecisionResponse response) {
        if (request == null){
            response.addValidationMessage("Credit Request is missing! Invalid request!");
            return;
        }

        if (request.getCreditAmount() <= 0) {
            response.addValidationMessage("Invalid loan amount!");
        }

        if (request.getSsn().isEmpty()) {
            response.addValidationMessage("No SSN Provided!");
        }

        if (request.getPeriodInMonths() <= 0) {
            response.addValidationMessage("Invalid period provided!");
        }
    }

    private void processCreditRequest(CreditCalculationRequest request, CreditDecisionResponse response) {
        int creditModifier = request.getCreditModifier();
        int requestedPeriod = request.getPeriodInMonths();
        double maxCreditAmount = calculateMaxCreditAmountInPeriod(creditModifier, requestedPeriod);

        if (maxCreditAmount > 0){
            response.setCreditDecisionForRequestedPeriod(new CreditDecisionItem(maxCreditAmount, requestedPeriod));
        }
        if (maxCreditAmount < request.getCreditAmount()) {
            int minPeriodForRequestedAmount = calculateMinLoanPeriod(creditModifier, request.getCreditAmount());
            if (minPeriodForRequestedAmount > 0){
                response.setApprovedCreditDecisionWithMinPeriod(new CreditDecisionItem(request.getCreditAmount(), minPeriodForRequestedAmount));
            }
        }
    }

    private double calculateCreditScore(int creditModifier, double creditAmount, int periodInMonths) {
        return (creditModifier / creditAmount) * periodInMonths;
    }

    private double calculateMaxCreditAmountInPeriod(int creditModifier, int periodInMonths) {
        int maxAmount = creditModifier * periodInMonths;

        if (maxAmount > maxCreditAmount) {
            return maxCreditAmount;
        }
        if (maxAmount < minCreditAmount) {
            return 0;
        }

        return maxAmount;
    }

    private int calculateMinLoanPeriod(int creditModifier, double creditAmount) {
        int minPeriod = (int)Math.ceil(creditAmount / creditModifier);

        if (minPeriod > maxCreditPeriodInMonths) {
            return 0;
        }

        if (minPeriod < minCreditPeriodInMonths) {
            return minCreditPeriodInMonths;
        }

        return minPeriod;
    }

    private double getValidRequestedAmount(double requestedAmount) {
        if (requestedAmount > maxCreditAmount) {
            requestedAmount = maxCreditAmount;
        }

        if (requestedAmount < minCreditAmount) {
            requestedAmount = minCreditAmount;
        }
        return requestedAmount;
    }

    private int getValidRequestPeriod(int periodInMonths) {
        if (periodInMonths > maxCreditPeriodInMonths) {
            periodInMonths = maxCreditPeriodInMonths;
        }

        if (periodInMonths < minCreditPeriodInMonths) {
            periodInMonths = minCreditPeriodInMonths;
        }
        return periodInMonths;
    }
}