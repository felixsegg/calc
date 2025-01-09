package main.java.logic.util.token;

import main.java.exception.TermSyntaxException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    public static List<Token> tokenize(String term) {
        List<Token> result = new ArrayList<>();
        
        for (int i = 0; i < term.length(); ) {
            Token newToken;
            
            if (Character.isDigit(term.charAt(i)) || term.charAt(i) == '.' || term.charAt(i) == 'n') {
                // n indicates a negative number, see preprocessing method in the CalculationService.
                // If the currently considered token is numerical
                StringBuilder numberString = new StringBuilder();
                
                // Iterate over term-String until the current number token is captured entirely
                do numberString.append(term.charAt(i++));
                while (i < term.length() && (Character.isDigit(term.charAt(i)) || term.charAt(i) == '.'));
                
                // NumberFormatException might occur in the following statement, is caught in HomeController.equalsClick()
                newToken = new NumberToken(new BigDecimal(numberString.toString().replace('n', '-')));
            } else
                // If the currently considered token is not numerical
                newToken = switch (term.charAt(i++)) {
                    case '-', '×', '÷', '+', '√' -> new OperationToken(term.charAt(i-1)); // index already incremented in head (2 lines above), therefore -1
                    case '(' -> new OpenParentToken();
                    case ')' -> new CloseParentToken();
                    default ->
                            throw new TermSyntaxException("An unexpected character has been detected: " + term.charAt(i-1) + ". Maybe something in the preprocessing went wrong.");
                };
            result.add(newToken);
        }
        return result;
    }
}
