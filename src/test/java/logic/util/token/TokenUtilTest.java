package logic.util.token;

import exception.TermSyntaxException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TokenUtilTest {
    
    // Tokenizer tests
    @Test
    void shouldReturnEmptyList() {
        String input = "";
        
        List<Token> tokens = TokenUtil.tokenize(input);
        
        assertTrue(tokens.isEmpty());
    }
    
    @Test
    void shouldReturnOpenParentClosedParent() {
        String input = "()";
        List<Token> desiredTokens = List.of(
                new OpenParentToken(),
                new CloseParentToken()
        );
        
        List<Token> actualTokens = TokenUtil.tokenize(input);
        
        assertEquals(desiredTokens, actualTokens);
    }
    
    @Test
    void shouldReturnSingularPositiveNumberToken() {
        String input = "1.234";
        List<Token> desiredTokens = List.of(
                new NumberToken("1.234")
        );
        
        List<Token> actualTokens = TokenUtil.tokenize(input);
        
        assertEquals(desiredTokens, actualTokens);
    }
    
    @Test
    void shouldReturnNegativeNumberToken() {
        String input = "n1.234";
        List<Token> desiredTokens = List.of(
                new OperationToken('n'),
                new NumberToken("1.234")
        );
        
        List<Token> actualTokens = TokenUtil.tokenize(input);
        
        assertEquals(desiredTokens, actualTokens);
    }
    
    @Test
    void shouldReturnSqrtToken() {
        String input = "√0.1";
        List<Token> desiredTokens = List.of(
                new OperationToken('√'),
                new NumberToken("0.1")
        );
        
        List<Token> actualTokens = TokenUtil.tokenize(input);
        
        assertEquals(desiredTokens, actualTokens);
    }
    
    @Test
    void shouldReturnInvertedNumber() {
        String input = "1*i2";
        List<Token> desiredTokens = List.of(
                new NumberToken("1"),
                new OperationToken('*'),
                new OperationToken('i'),
                new NumberToken("2")
        );
        
        List<Token> actualTokens = TokenUtil.tokenize(input);
        
        assertEquals(desiredTokens, actualTokens);
    }
    
    @Test
    void shouldReturnAccordinglyComplicatedTerm1() {
        String input = "((1+2)*i(3.09+n4.7)+12)";
        List<Token> desiredTokens = List.of(
                new OpenParentToken(),
                new OpenParentToken(),
                new NumberToken("1"),
                new OperationToken('+'),
                new NumberToken("2"),
                new CloseParentToken(),
                new OperationToken('*'),
                new OperationToken('i'),
                new OpenParentToken(),
                new NumberToken("3.09"),
                new OperationToken('+'),
                new OperationToken('n'),
                new NumberToken("4.7"),
                new CloseParentToken(),
                new OperationToken('+'),
                new NumberToken("12"),
                new CloseParentToken()
                );
        
        List<Token> actualTokens = TokenUtil.tokenize(input);
        
        assertEquals(desiredTokens, actualTokens);
    }
    
    @Test
    void shouldThrowTermSyntaxExceptionBecauseOfUnexpectedCharacter() {
        String input = "1.234ä";
        
        assertThrows(TermSyntaxException.class, () -> TokenUtil.tokenize(input));
    }
    
    
    // isEncapsulated() tests
    
    @Test
    void shouldReturnTrueTwoParentheses() {
        List<Token> tokens = List.of(
                new OpenParentToken(),
                new CloseParentToken()
        );
        
        boolean isEncapsulated = TokenUtil.isEncapsulated(tokens);
        
        assertTrue(isEncapsulated);
    }
    
    @Test
    void shouldReturnTrueComplicatedTerm1() {
        List<Token> tokens = List.of(
                new OpenParentToken(),
                new OpenParentToken(),
                new NumberToken("1"),
                new OperationToken('+'),
                new NumberToken("2"),
                new CloseParentToken(),
                new OperationToken('*'),
                new OperationToken('i'),
                new OpenParentToken(),
                new NumberToken("3.09"),
                new OperationToken('+'),
                new OperationToken('n'),
                new NumberToken("4.7"),
                new CloseParentToken(),
                new OperationToken('+'),
                new NumberToken("12"),
                new CloseParentToken()
        );
        
        boolean isEncapsulated = TokenUtil.isEncapsulated(tokens);
        
        assertTrue(isEncapsulated);
    }
    
    @Test
    void shouldReturnTrueComplicatedTerm2() {
        List<Token> tokens = List.of(
                new OpenParentToken(),
                new OpenParentToken(),
                new NumberToken("1"),
                new OperationToken('+'),
                new NumberToken("2"),
                new CloseParentToken(),
                new OperationToken('*'),
                new OpenParentToken(),
                new NumberToken("3.09"),
                new OperationToken('+'),
                new OperationToken('n'),
                new NumberToken("4.7"),
                new CloseParentToken(),
                new CloseParentToken()
        );
        
        boolean isEncapsulated = TokenUtil.isEncapsulated(tokens);
        
        assertTrue(isEncapsulated);
    }
    
    @Test
    void shouldReturnFalseComplicatedTerm1() {
        List<Token> tokens = List.of(
                new OpenParentToken(),
                new NumberToken("1"),
                new OperationToken('+'),
                new NumberToken("2"),
                new CloseParentToken(),
                new OperationToken('*'),
                new OperationToken('i'),
                new OpenParentToken(),
                new NumberToken("3.09"),
                new OperationToken('+'),
                new OperationToken('n'),
                new NumberToken("4.7"),
                new CloseParentToken(),
                new OperationToken('+'),
                new NumberToken("12")
        );
        
        boolean isEncapsulated = TokenUtil.isEncapsulated(tokens);
        
        assertFalse(isEncapsulated);
    }
}
