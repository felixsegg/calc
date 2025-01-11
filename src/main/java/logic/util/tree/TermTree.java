package main.java.logic.util.tree;

import main.java.exception.TermSyntaxException;
import main.java.logic.util.token.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TermTree {
    private final Node root;
    
    private TermTree(List<Token> tokens) {
        this.root = build(tokens);
    }
    
    private static Node build(List<Token> tokens) {
        DummyNode dummy = new DummyNode(tokens);
        return dummy.build();
    }
    
    private BigDecimal evaluate() {
        if (root == null) return BigDecimal.ZERO;
        BigDecimal result = root.evaluate();
        if (result != null) return root.evaluate();
        else return BigDecimal.ZERO;
    }
    
    public static BigDecimal evaluate(List<Token> tokens) {
        try {
            TermTree tree = new TermTree(tokens);
            return tree.evaluate();
        } catch (Exception ex) {
            throw new TermSyntaxException("Something went wrong while building the evaluation tree, indicating a syntax error.");
        }
    }
}
