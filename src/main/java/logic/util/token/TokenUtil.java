package logic.util.token;

import exception.TermSyntaxException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TokenUtil {
    public static List<Token> tokenize(String term) {
        List<Token> result = new ArrayList<>();
        
        for (int i = 0; i < term.length(); ) {
            Token newToken;
            
            if (Character.isDigit(term.charAt(i)) || term.charAt(i) == '.') {
                // If the currently considered token is numerical
                StringBuilder numberString = new StringBuilder();
                
                // Iterate over term-String until the current number token is captured entirely
                do numberString.append(term.charAt(i++));
                while (i < term.length() && (Character.isDigit(term.charAt(i)) || term.charAt(i) == '.'));
                
                // NumberFormatException might occur in the following statement, is caught in HomeController.equalsClick()
                newToken = new NumberToken(new BigDecimal(numberString.toString()));
            } else
                // If the currently considered token is not numerical
                newToken = switch (term.charAt(i++)) {
                    case '*', '+', '√', 'n', 'i' ->
                            new OperationToken(term.charAt(i - 1)); // index already incremented in head (2 lines above), therefore -1
                    case '(' -> new OpenParentToken();
                    case ')' -> new CloseParentToken();
                    default ->
                            throw new TermSyntaxException("An unexpected character has been detected: " + term.charAt(i - 1) + ". Maybe something in the preprocessing went wrong.");
                };
            result.add(newToken);
        }
        return result;
    }
    
    /**
     * Checks whether a given token list is encapsulated in parentheses which form a pair. For example: ((1+2)÷(3+4))
     * would return true. On the other hand (1+2)÷(3+4) would return false. Obviously simply checking if the first and
     * last character are '(' respectively ')' isn't sufficient.
     *
     * @param tokens the list of tokens
     * @return {@code true}, if the first and last tokens are an opening and closing parenthesis which belong together,
     * {@code false} otherwise.
     */
    public static boolean isEncapsulated(List<Token> tokens) {
        if (tokens.size() < 2
                || tokens.get(0).getTokenType() != TokenType.OPENING_PARENTHESIS
                || tokens.get(tokens.size() - 1).getTokenType() != TokenType.CLOSING_PARENTHESIS)
            return false;
        
        // We can start the loop at index 1 and stop it early at the next to last item, because we already checked these
        // in the opening conditional of this method.
        int currentNestingDepth = 1;
        for (int i = 1; i < tokens.size() - 1; i++) {
            if (tokens.get(i).getTokenType() == TokenType.CLOSING_PARENTHESIS && --currentNestingDepth < 1)
                return false;
            if (tokens.get(i).getTokenType() == TokenType.OPENING_PARENTHESIS)
                currentNestingDepth++;
        }
        return true;
    }
}
