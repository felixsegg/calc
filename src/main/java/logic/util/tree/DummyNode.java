package logic.util.tree;

import exception.DummyEvaluationException;
import exception.TermSyntaxException;
import logic.util.Operation;
import logic.util.token.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DummyNode extends Node {
    private final List<Token> tokenList;
    
    protected DummyNode(List<Token> tokenList) {
        while (TokenUtil.isEncapsulated(tokenList))
            tokenList = tokenList.subList(1, tokenList.size() - 1);
        
        if (tokenList.isEmpty())
            throw new IllegalArgumentException("Token list may not be empty (or consist only of empty Parentheses).");
        
        this.tokenList = tokenList;
    }
    
    protected List<Token> getTokenList() {
        return tokenList;
    }
    
    private static Node generateNode(List<List<Token>> subTokenLists) {
        List<Node> additionNodeChildren = new ArrayList<>();
        
        // Currently, the last operand gets ignored. Add final + 0, so this won't happen.
        // TODO: Change that solution, that's ugly.
        List<Token> plus = new ArrayList<>();
        plus.add(new OperationToken('+'));
        List<Token> zero = new ArrayList<>();
        zero.add(new NumberToken(BigDecimal.ZERO));
        subTokenLists.add(plus);
        subTokenLists.add(zero);
        
        for (int i = 0; i < subTokenLists.size(); i++) {
            if (subTokenLists.get(i).size() > 1 // Can't be singular operator token
                    || !(subTokenLists.get(i).get(0) instanceof OperationToken o) // Can't be singular operator token
                    || o.getOpType() != Operation.PLUS && o.getOpType() != Operation.MULTIPLY) // Isn't elementary function
                continue;
            
            if (o.getOpType() == Operation.PLUS)
                additionNodeChildren.add(collapseTermWithUnaries(subTokenLists, i - 1));
            else if (o.getOpType() == Operation.MULTIPLY) {
                // Multiplication sequence (PEMDAS)
                List<Node> multiplicationNodeChildren = new ArrayList<>();
                multiplicationNodeChildren.add(collapseTermWithUnaries(subTokenLists, i - 1));
                for (; i < subTokenLists.size(); ++i) {
                    if (subTokenLists.get(i).size() > 1 // Can't be singular operator token
                            || !(subTokenLists.get(i).get(0) instanceof OperationToken op) // Can't be singular operator token
                            || op.getOpType() != Operation.PLUS && op.getOpType() != Operation.MULTIPLY) // Isn't elementary function
                        continue;
                    
                    // Token has to be either addition or multiplication, meaning the previous token has to be a term bounded to multiplication
                    multiplicationNodeChildren.add(collapseTermWithUnaries(subTokenLists, i - 1));
                    
                    if (op.getOpType() == Operation.PLUS) break;
                }
                additionNodeChildren.add(new Inode(Operation.MULTIPLY, multiplicationNodeChildren));
            }
        }
        
        return switch (additionNodeChildren.size()) {
            case 0 -> collapseTermWithUnaries(subTokenLists, subTokenLists.size());
            case 1 -> additionNodeChildren.get(0);
            default -> new Inode(Operation.PLUS, additionNodeChildren);
        };
    }
    
    private List<List<Token>> generateSubTokenLists() {
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
                        else if (subTermToken.getTokenType() == TokenType.OPENING_PARENTHESIS)
                            currentNestingDepth++;
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
        DummyNode dummy = new DummyNode(subTokenLists.get(index));
        Node result = dummy.build();
        
        for (int i = index; i >= 0; --i) {
            if (subTokenLists.get(i).size() > 1
                    || !(subTokenLists.get(i).get(0) instanceof OperationToken o)
                    || o.getOpType() != Operation.SQRT && o.getOpType() != Operation.NEGATE && o.getOpType() != Operation.INVERT)
                break;
            
            // Create new unary operation Inode with the previous Inode as a child
            result = new Inode(o.getOpType(), result);
        }
        
        return result;
    }
    
    protected Node build() {
        try {
            return switch (tokenList.size()) {
                case 0 -> throw new RuntimeException("Something went terribly wrong.");
                case 1 ->
                        new Leaf(((NumberToken) tokenList.get(0)).getValue()); // Can logically only be a singular number if the syntax of the term was correct
                default -> generateNode(generateSubTokenLists());
            };
        } catch (ClassCastException ex) {
            throw new TermSyntaxException("A singular Token which is not a number does not make sense.");
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new TermSyntaxException("Unexpected token.");
        }
    }
    
    @Override
    public BigDecimal evaluate() {
        throw new DummyEvaluationException("Dummies should only be placeholders during generation of a tree and therefore not evaluated.");
    }
}
