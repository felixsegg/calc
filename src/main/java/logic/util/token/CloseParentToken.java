package logic.util.token;

public class CloseParentToken extends Token {
    @Override
    public TokenType getTokenType() {
        return TokenType.CLOSING_PARENTHESIS;
    }
}
