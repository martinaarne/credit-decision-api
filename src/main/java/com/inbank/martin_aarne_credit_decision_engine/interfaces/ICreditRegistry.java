package com.inbank.martin_aarne_credit_decision_engine.interfaces;

import com.inbank.martin_aarne_credit_decision_engine.models.CreditModifierResponse;

public interface ICreditRegistry {
    CreditModifierResponse getCreditModifier(String ssn);
}
