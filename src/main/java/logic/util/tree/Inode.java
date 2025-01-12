package logic.util.tree;

import logic.util.Operation;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Inode extends Node {
    private final Operation opType;
    private final List<Node> children = new ArrayList<>();
    
    protected Inode(Operation opType, Node... children) {
        if (opType == Operation.PLUS && children.length < 2
                || opType == Operation.MULTIPLY && children.length < 2
                || opType == Operation.SQRT && children.length != 1
                || opType == Operation.NEGATE && children.length != 1
                || opType == Operation.INVERT && children.length != 1
                || opType == null)
            throw new IllegalArgumentException("Operation doesn't fit the operand count or is null. opType: " + opType + ", operand count: " + children.length);
        
        // opType null means, that the node has been created as a precaution.
        
        this.opType = opType;
        this.children.addAll(Arrays.asList(children));
    }
    
    protected Inode(Operation opType, List<Node> children) {
        if (opType == Operation.PLUS && children.size() < 2
                || opType == Operation.MULTIPLY && children.size() < 2
                || opType == Operation.SQRT && children.size() != 1
                || opType == Operation.NEGATE && children.size() != 1
                || opType == Operation.INVERT && children.size() != 1
                || opType == null)
            throw new IllegalArgumentException("Operation doesn't fit the operand count or is null. opType: " + opType + ", operand count: " + children.size());
        
        // opType null means, that the node has been created as a precaution.
        
        this.opType = opType;
        this.children.addAll(children);
    }
    
    @Override
    protected BigDecimal evaluate() {
        return switch (opType) {
            case PLUS -> children.stream().map(Node::evaluate).reduce(BigDecimal.ZERO, BigDecimal::add);
            case NEGATE -> children.get(0).evaluate().negate();
            case MULTIPLY -> children.stream().map(Node::evaluate).reduce(BigDecimal.ONE, BigDecimal::multiply);
            case INVERT -> BigDecimal.ONE.divide(children.get(0).evaluate(), MathContext.DECIMAL64);
            case SQRT -> children.get(0).evaluate().sqrt(MathContext.DECIMAL64);
        };
    }
}
