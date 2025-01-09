package main.java.logic.util.token;

public class ElementaryOpToken extends Token {
    public enum Operation {
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE
    }
    
    
    private final Operation opType;
    
    protected ElementaryOpToken(char c) {
        opType = switch (c) {
            case '+' -> Operation.PLUS;
            case '-' -> Operation.MINUS;
            case '×' -> Operation.MULTIPLY;
            case '÷' -> Operation.DIVIDE;
            default -> throw new IllegalArgumentException("Tried to construct ElementaryOpToken with something different than the legal characters.");
        };
    }
    
    public Operation getOpType() {
        return opType;
    }
}
