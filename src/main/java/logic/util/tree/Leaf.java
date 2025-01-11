package main.java.logic.util.tree;

import java.math.BigDecimal;

public class Leaf extends Node{
    private final BigDecimal value;
    
    protected Leaf(BigDecimal value) {
        this.value = value;
    }
    @Override
    public BigDecimal evaluate() {
        return value;
    }
}
