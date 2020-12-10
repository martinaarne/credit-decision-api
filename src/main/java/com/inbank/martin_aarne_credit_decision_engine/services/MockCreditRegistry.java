package com.inbank.martin_aarne_credit_decision_engine.services;

import com.inbank.martin_aarne_credit_decision_engine.interfaces.ICreditRegistry;
import com.inbank.martin_aarne_credit_decision_engine.models.CreditModifierResponse;
import org.springframework.stereotype.Component;

@Component
public class MockCreditRegistry implements ICreditRegistry {
    public MockCreditRegistry(){

    }
    @Override
    public CreditModifierResponse getCreditModifier(String ssn) {
        CreditModifierResponse response = new CreditModifierResponse();
        response.setSsn(ssn);

        if (ssn.equals("49002010965")){
            response.setCreditRating(0);
        }

        if (ssn.equals("49002010976")) {
            response.setCreditRating(100);
        }

        if (ssn.equals("49002010987")) {
            response.setCreditRating(300);
        }

        if (ssn.equals("49002010998")) {
            response.setCreditRating(1000);
        }

        return response;
    }
}
