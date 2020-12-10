package com.inbank.martin_aarne_credit_decision_engine.web;

import com.inbank.martin_aarne_credit_decision_engine.interfaces.ICreditDecisionService;
import com.inbank.martin_aarne_credit_decision_engine.models.CreditDecisionResponse;
import com.inbank.martin_aarne_credit_decision_engine.models.CreditRequest;
import org.springframework.web.bind.annotation.*;

@RestController
public class CreditController {
    public CreditController(ICreditDecisionService creditDecisionService) {
        this.creditDecisionService = creditDecisionService;
    }

    private ICreditDecisionService creditDecisionService;

    @PostMapping("/credit-decision")
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:3000")
    public CreditDecisionResponse evaluateCreditRequest(@RequestBody CreditRequest request) {
        return creditDecisionService.processCreditRequest(request);
    }
}
