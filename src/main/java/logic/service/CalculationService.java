package main.java.logic.service;

import main.java.logic.engine.EvaluationEngine;

import java.math.BigDecimal;

public class CalculationService {
    private static final CalculationService instance = new CalculationService();
    
    private CalculationService() {}
    
    public static CalculationService getInstance() {
        return instance;
    }
    
    public BigDecimal evaluate(String input, BigDecimal ans) {
        return EvaluationEngine.evaluate(input, ans);
    }
    
    
}
