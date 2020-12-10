package com.inbank.martin_aarne_credit_decision_engine.interfaces;

import com.inbank.martin_aarne_credit_decision_engine.models.CreditDecisionResponse;
import com.inbank.martin_aarne_credit_decision_engine.models.CreditRequest;

public interface ICreditDecisionService {
    CreditDecisionResponse processCreditRequest(CreditRequest request);
}
