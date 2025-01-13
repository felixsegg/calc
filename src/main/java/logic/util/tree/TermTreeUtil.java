package logic.util.tree;

import exception.TermSyntaxException;
import logic.util.Operation;
import logic.util.token.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TermTreeUtil {
    public static BigDecimal evaluate(List<Token> tokens) {
        Node root = TermTreeUtil.build(tokens);
        if (root == null) return BigDecimal.ZERO;
        BigDecimal result = root.evaluate();
        
        return result != null
                ? root.evaluate()
                : BigDecimal.ZERO;
    }
    
    private static Node generateNode(List<List<Token>> subTokenLists) {
        List<Node> additionNodeChildren = new ArrayList<>();
        
        // Currently, the last operand gets ignored. Add final + 0, so this won't happen.
        // TODO: Change this solution, it's ugly.
        List<Token> plus = new ArrayList<>();
        plus.add(new OperationToken('+'));
        List<Token> zero = new ArrayList<>();
        zero.add(new NumberToken("0"));
        subTokenLists.add(plus);
        subTokenLists.add(zero);
        
        for (int i = 0; i < subTokenLists.size(); i++) {
            if (subTokenLists.get(i).size() > 1 // Can't be singular operator token
                    || !(subTokenLists.get(i).get(0) instanceof OperationToken o) // Can't be singular operator token
                    || o.getOpType() != Operation.ADDITION && o.getOpType() != Operation.MULTIPLY) // Isn't elementary function
                continue;
            
            if (o.getOpType() == Operation.ADDITION)
                additionNodeChildren.add(collapseTermWithUnaries(subTokenLists, i - 1));
            else if (o.getOpType() == Operation.MULTIPLY) {
                // Multiplication sequence (PEMDAS)
                List<Node> multiplicationNodeChildren = new ArrayList<>();
                for (; i < subTokenLists.size(); i++) {
                    if (subTokenLists.get(i).size() > 1 // Can't be singular operator token
                            || !(subTokenLists.get(i).get(0) instanceof OperationToken op) // Can't be singular operator token
                            || op.getOpType() != Operation.ADDITION && op.getOpType() != Operation.MULTIPLY) // Isn't elementary function
                        continue;
                    
                    // Token has to be either addition or multiplication, meaning the previous token has to be a term bounded to multiplication
                    multiplicationNodeChildren.add(collapseTermWithUnaries(subTokenLists, i - 1));
                    
                    if (op.getOpType() == Operation.ADDITION) break;
                }
                additionNodeChildren.add(new Inode(Operation.MULTIPLY, multiplicationNodeChildren));
            }
        }
        
        return switch (additionNodeChildren.size()) {
            case 0 -> collapseTermWithUnaries(subTokenLists, subTokenLists.size());
            case 1 -> additionNodeChildren.get(0);
            default -> new Inode(Operation.ADDITION, additionNodeChildren);
        };
    }
    
    private static List<List<Token>> generateSubTokenLists(List<Token> tokenList) {
        List<List<Token>> subTokenLists = new ArrayList<>();
        
        for (int i = 0; i < tokenList.size(); i++) {
            Token current = tokenList.get(i);
            int fromIndex = i;
            int toIndex;
            
            switch (current.getTokenType()) {
                case NUMBER, OPERATION -> toIndex = i + 1; // operation
                case OPENING_PARENTHESIS -> { // subterm
                    int currentNestingDepth = 1;
                    while (true) {
                        Token subTermToken = tokenList.get(++i);
                        if (subTermToken.getTokenType() == TokenType.CLOSING_PARENTHESIS && --currentNestingDepth < 1)
                            break;
                        else if (subTermToken.getTokenType() == TokenType.OPENING_PARENTHESIS) currentNestingDepth++;
                    }
                    toIndex = i + 1;
                }
                default -> throw new TermSyntaxException("Unexpected token");
            }
            subTokenLists.add(tokenList.subList(fromIndex, toIndex));
        }
        
        return subTokenLists;
    }
    
    
    private static Node collapseTermWithUnaries(List<List<Token>> subTokenLists, int index) {
        List<Token> consideredTokenList = subTokenLists.get(index);
        
        while (TokenUtil.isEncapsulated(consideredTokenList)) {
            consideredTokenList = consideredTokenList.subList(1, consideredTokenList.size() - 1);
        }
        
        Node result = TermTreeUtil.build(consideredTokenList);
        if(index < 1) return result;
        
        for (int i = index - 1; i >= 0; i--) {
            if (subTokenLists.get(i).size() > 1
                    || !(subTokenLists.get(i).get(0) instanceof OperationToken o)
                    || o.getOpType() != Operation.SQRT && o.getOpType() != Operation.NEGATE && o.getOpType() != Operation.INVERT)
                break;
            
            // Create new unary operation Inode with the previous Inode as a child
            result = new Inode(o.getOpType(), result);
        }
        
        return result;
    }
    
    protected static Node build(List<Token> tokens) {
        try {
            return switch (tokens.size()) {
                case 0 -> throw new RuntimeException("Something went terribly wrong.");
                // Case 1 can logically only be a singular number if the syntax of the term was correct
                case 1 -> new Leaf(((NumberToken) tokens.get(0)).getValue());
                default -> generateNode(generateSubTokenLists(tokens));
            };
        } catch (ClassCastException ex) {
            throw new TermSyntaxException("A singular Token which is not a number does not make sense.");
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new TermSyntaxException("Unexpected token.");
        }
    }
}
