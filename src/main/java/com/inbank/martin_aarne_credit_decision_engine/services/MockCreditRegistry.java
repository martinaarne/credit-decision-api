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
        Integer creditRating = getCreditRatingFromSsn(ssn);
        if(creditRating == null){
            return null;
        }

        CreditModifierResponse response = new CreditModifierResponse();
        response.setSsn(ssn);
        response.setCreditRating(creditRating);
        return response;
    }

    private Integer getCreditRatingFromSsn(String ssn) {
        if (ssn.equals("49002010965")){
            return 0;
        }

        if (ssn.equals("49002010976")) {
            return 100;
        }

        if (ssn.equals("49002010987")) {
            return 300;
        }

        if (ssn.equals("49002010998")) {
            return 1000;
        }

        return null;
    }
}
