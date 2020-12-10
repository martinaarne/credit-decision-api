package com.inbank.martin_aarne_credit_decision_engine.models;

public class CreditCalculationRequest {
    private double creditAmount;
    private int periodInMonths;
    private int creditModifier;

    public double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public int getPeriodInMonths() {
        return periodInMonths;
    }

    public void setPeriodInMonths(int periodInMonths) {
        this.periodInMonths = periodInMonths;
    }

    public int getCreditModifier() {
        return creditModifier;
    }

    public void setCreditModifier(int creditModifier) {
        this.creditModifier = creditModifier;
    }
}
