package logic.util.tree;

import java.math.BigDecimal;
import java.util.Objects;

public class Leaf extends Node{
    private final BigDecimal value;
    
    protected Leaf(BigDecimal value) {
        this.value = value;
    }
    @Override
    public BigDecimal evaluate() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Leaf leaf = (Leaf) o;
        return value.compareTo(leaf.value) == 0;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
