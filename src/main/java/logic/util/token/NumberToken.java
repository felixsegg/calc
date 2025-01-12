package logic.util.token;

import java.math.BigDecimal;
import java.math.MathContext;

public class NumberToken extends Token {
    private BigDecimal value;
    public NumberToken(BigDecimal value) {
        if (value == null)
            throw new IllegalArgumentException("value must not be null.");
        this.value = value;
    }
    
    public BigDecimal getValue() {
        return value;
    }
    
    public void negate() {
        value = value.negate();
    }
    
    public void invert() {
        value = BigDecimal.ONE.divide(value, MathContext.DECIMAL64);
    }
    
    @Override
    public TokenType getTokenType() {
        return TokenType.NUMBER;
    }
}
