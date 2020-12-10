package com.inbank.martin_aarne_credit_decision_engine.models;

import java.util.ArrayList;
import java.util.List;

public class CreditDecisionResponse {
    private CreditRequest request;
    private CreditDecisionItem creditDecisionForRequestedPeriod;
    private CreditDecisionItem approvedCreditDecisionWithMinPeriod;
    private List<String> validationMessages;

    public CreditDecisionResponse() {
        validationMessages = new ArrayList<>();
    }

    public CreditDecisionItem getCreditDecisionForRequestedPeriod() {
        return creditDecisionForRequestedPeriod;
    }

    public void setCreditDecisionForRequestedPeriod(CreditDecisionItem creditDecisionForRequestedPeriod) {
        this.creditDecisionForRequestedPeriod = creditDecisionForRequestedPeriod;
    }

    public CreditDecisionItem getApprovedCreditDecisionWithMinPeriod() {
        return approvedCreditDecisionWithMinPeriod;
    }

    public void setApprovedCreditDecisionWithMinPeriod(CreditDecisionItem approvedCreditDecisionForMinPeriod) {
        this.approvedCreditDecisionWithMinPeriod = approvedCreditDecisionForMinPeriod;
    }

    public CreditRequest getRequest() {
        return request;
    }

    public void setRequest(CreditRequest request) {
        this.request = request;
    }

    public void addValidationMessage(String message){
        validationMessages.add(message);
    }

    public List<String> getValidationMessages(){
        return this.validationMessages;
    }

    public boolean isValid(){
        return this.validationMessages.size() == 0;
    }
}

