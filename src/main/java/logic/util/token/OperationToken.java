package main.java.logic.util.token;

import main.java.logic.util.Operation;

public class OperationToken extends Token {
    
    
    private final Operation opType;
    
    protected OperationToken(char c) {
        opType = switch (c) {
            case '+' -> Operation.PLUS;
            case '*' -> Operation.MULTIPLY;
            case '√' -> Operation.SQRT;
            case 'n' -> Operation.NEGATE;
            case 'i' -> Operation.INVERT;
            default -> throw new IllegalArgumentException("Tried to construct OperationToken with something different than the legal characters.");
        };
    }
    
    public Operation getOpType() {
        return opType;
    }
    
    @Override
    public TokenType getTokenType() {
        return TokenType.OPERATION;
    }
}
