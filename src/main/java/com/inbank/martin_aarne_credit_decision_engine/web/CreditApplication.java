package com.inbank.martin_aarne_credit_decision_engine.web;

import com.inbank.martin_aarne_credit_decision_engine.interfaces.ICreditRegistry;
import com.inbank.martin_aarne_credit_decision_engine.services.CreditDecisionService;
import com.inbank.martin_aarne_credit_decision_engine.services.MockCreditRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CreditApplication {

    public static void main(String[] args) {
        SpringApplication.run(CreditApplication.class, args);
    }

    @Bean
    public CreditDecisionService creditDecisionService() {
        return new CreditDecisionService(this.creditRegistry());
    }

    @Bean
    public ICreditRegistry creditRegistry() {
        return new MockCreditRegistry();
    }
}
