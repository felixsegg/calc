package main.java.logic.util.tree;

import main.java.logic.util.token.Token;

import java.math.BigDecimal;
import java.util.List;

public class TermTree {
    private Node root;
    private static TermTree build(List<Token> tokens) {
        // TODO
        return null;
    }
    
    private BigDecimal evaluate() {
        // TODO
        return BigDecimal.ZERO;
    }
    
    public static BigDecimal evaluate(List<Token> tokens) {
        TermTree tree = build(tokens);
        return tree.evaluate();
    }
}
