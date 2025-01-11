package main.java.logic.util.token;

public class OpenParentToken extends Token {
    @Override
    public TokenType getTokenType() {
        return TokenType.OPENING_PARENTHESIS;
    }
}
