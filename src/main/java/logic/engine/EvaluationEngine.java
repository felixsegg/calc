package main.java.logic.engine;

import main.java.logic.util.token.Token;
import main.java.logic.util.token.Tokenizer;
import main.java.logic.util.tree.TermTree;

import java.math.BigDecimal;
import java.util.List;

public class EvaluationEngine {
    public static BigDecimal evaluate(String input, BigDecimal ans) {
        String term = preprocess(input, ans);
        List<Token> tokens = Tokenizer.tokenize(term);
        return TermTree.evaluate(tokens);
    }
    
    private static String preprocess(String input, BigDecimal ans) {
        // TODO: Syntax check.
        // TODO: Check whether there are enough closing parenthesis and fill them in at the end if needed
        // TODO: Replace number negating minus characters  "(-" with "(n" to indicate negativity and avoid confusion
        // TODO: Replace max number of leading and closing parentheses without changing semantic meaning
        return "";
    }
}
