package logic.util.tree;

import logic.util.Operation;
import logic.util.token.NumberToken;
import logic.util.token.OperationToken;
import logic.util.token.Token;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TermTreeUtilTest {
    private static Method collapseTermWithUnaries = null;
    
    private static Node collapseTermWithUnaries(List<List<Token>> subTokenLists, int index) {
        try {
            if (collapseTermWithUnaries == null) {
                collapseTermWithUnaries = TermTreeUtil.class
                        .getDeclaredMethod("collapseTermWithUnaries", List.class, int.class);
                collapseTermWithUnaries.setAccessible(true);
            }
            
            return (Node) collapseTermWithUnaries.invoke(null, subTokenLists, index);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Test
    void shouldReturnNegatedNumber() {
        List<List<Token>> input = List.of(
                List.of(new OperationToken('n')),
                List.of(new NumberToken("2"))
        );
        
        Node desired = new Inode(Operation.NEGATE, new Leaf(new BigDecimal("2")));
        
        Node actual = collapseTermWithUnaries(input, 1);
        
        assertEquals(desired, actual);
    }
    
    @Test
    void shouldReturnInvertedNumber() {
        List<List<Token>> input = List.of(
                List.of(new OperationToken('i')),
                List.of(new NumberToken("2"))
        );
        
        Node desired = new Inode(Operation.INVERT, new Leaf(new BigDecimal("2")));
        
        Node actual = collapseTermWithUnaries(input, 1);
        
        assertEquals(desired, actual);
    }
    
    @Test
    void shouldReturnSqrtNumber() {
        List<List<Token>> input = List.of(
                List.of(new OperationToken('√')),
                List.of(new NumberToken("2"))
        );
        
        Node desired = new Inode(Operation.SQRT, new Leaf(new BigDecimal("2")));
        
        Node actual = collapseTermWithUnaries(input, 1);
        
        assertEquals(desired, actual);
    }
    
    @Test
    void shouldReturnSqrtNegNumber() {
        List<List<Token>> input = List.of(
                List.of(new OperationToken('√')),
                List.of(new OperationToken('n')),
                List.of(new NumberToken("2"))
        );
        
        Node desired = new Inode(Operation.SQRT,
                new Inode(Operation.NEGATE,
                        new Leaf(new BigDecimal("2"))
                ));
        
        Node actual = collapseTermWithUnaries(input, 2);
        
        assertEquals(desired, actual);
    }
    
    @Test
    void shouldReturnSqrtInvNumber() {
        List<List<Token>> input = List.of(
                List.of(new OperationToken('√')),
                List.of(new OperationToken('i')),
                List.of(new NumberToken("2"))
        );
        
        Node desired = new Inode(Operation.SQRT,
                new Inode(Operation.INVERT,
                        new Leaf(new BigDecimal("2"))
                ));
        
        Node actual = collapseTermWithUnaries(input, 2);
        
        assertEquals(desired, actual);
    }
    
    @Test
    void shouldReturnInvNegNumber() {
        List<List<Token>> input = List.of(
                List.of(new OperationToken('i')),
                List.of(new OperationToken('n')),
                List.of(new NumberToken("3.89"))
        );
        
        Node desired = new Inode(Operation.INVERT,
                new Inode(Operation.NEGATE,
                        new Leaf(new BigDecimal("3.89"))
                ));
        
        Node actual = collapseTermWithUnaries(input, 2);
        
        assertEquals(desired, actual);
    }
    
    @Test
    void shouldReturnInvSqrtNegNumber() {
        List<List<Token>> input = List.of(
                List.of(new OperationToken('i')),
                List.of(new OperationToken('√')),
                List.of(new OperationToken('n')),
                List.of(new NumberToken("3.89"))
        );
        
        Node desired = new Inode(Operation.INVERT,
                new Inode(Operation.SQRT,
                        new Inode(Operation.NEGATE,
                        new Leaf(new BigDecimal("3.89"))
                )));
        
        Node actual = collapseTermWithUnaries(input, 3);
        
        assertEquals(desired, actual);
    }
}
