package logic.util.token;

public abstract class Token {
    public abstract TokenType getTokenType();
    
    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass();
    }
}
