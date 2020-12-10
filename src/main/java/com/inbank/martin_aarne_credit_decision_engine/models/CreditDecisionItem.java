package com.inbank.martin_aarne_credit_decision_engine.models;

public class CreditDecisionItem {
    public int periodInMonths;
    public double amount;

    public CreditDecisionItem(double amount, int period) {
        this.periodInMonths = period;
        this.amount = amount;
    }

}
