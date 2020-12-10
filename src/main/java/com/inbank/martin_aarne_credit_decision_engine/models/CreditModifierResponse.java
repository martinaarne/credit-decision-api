package com.inbank.martin_aarne_credit_decision_engine.models;

public class CreditModifierResponse {
    private String ssn;
    private int creditRating;

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public int getCreditModifier() {
        return creditRating;
    }

    public void setCreditRating(int creditRating) {
        this.creditRating = creditRating;
    }
}
