package main.java.logic.util.token;

import java.math.BigDecimal;

public class NumberToken extends Token {
    private final BigDecimal value;
    protected NumberToken(BigDecimal value) {
        // value = null means this token represents "ans".
        this.value = value;
    }
    
    public BigDecimal getValue() {
        return value;
    }
    
    public boolean isAns() {
        return value == null;
    }
}
