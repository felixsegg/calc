package logic.util.token;

import logic.util.Operation;

public class OperationToken extends Token {
    private final Operation opType;
    
    public OperationToken(char c) {
        opType = switch (c) {
            case '+' -> Operation.ADDITION;
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
    public boolean equals(Object obj) {
        return obj instanceof OperationToken o && this.getOpType() == o.getOpType();
    }
    
    @Override
    public TokenType getTokenType() {
        return TokenType.OPERATION;
    }
}
