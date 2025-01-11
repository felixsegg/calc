package main.java.logic.engine;

import main.java.exception.TermSyntaxException;
import main.java.logic.util.token.Token;
import main.java.logic.util.token.TokenUtil;
import main.java.logic.util.tree.TermTree;

import java.math.BigDecimal;
import java.util.List;

public class EvaluationEngine {
    public static BigDecimal evaluate(String input, BigDecimal ans) {
        String term = preprocess(input, ans);
        List<Token> tokens = TokenUtil.tokenize(term);
        return TermTree.evaluate(tokens);
    }
    
    private static String preprocess(String input, BigDecimal ans) {
        // Insert ans into the term. If ans is negative, it has to be put into parentheses first.
        String ansString = ans.signum() < 0 ? "(" + ans + ")" : ans.toString();
        String term = input.replace("ans", ansString);
        
        // Replace × and ÷ with more common symbols * and /
        term = term.replace("×", "*").replace("÷", "/");
       
        // Check whether there are enough closing parenthesis and fill them in at the end if needed
        int openingCount = (int) term.chars().filter(ch -> ch == '(').count();
        int closingCount = (int) term.chars().filter(ch -> ch == ')').count();
        int parenthesisDifference = openingCount - closingCount;
        if (parenthesisDifference < 0)
            throw new TermSyntaxException("Somehow there are more closing than opening parenthesis.");
        else if (parenthesisDifference > 0)
            term = term + String.valueOf(')').repeat(parenthesisDifference);
        
        // Replace negative parentheses and roots with multiplication by -1
        // Replace - with n and ÷ with i, indicating negativity and inversion to calculate with inverse operations instead.
        term = term.replace("-(", "(-1)*(").replace("-√", "(-1)*√");
        term = term.replace("(-", "(0-");
        term = term. replace("-", "+n");
        term = term.replace("/", "*i");
        
        return term;
    }
}
