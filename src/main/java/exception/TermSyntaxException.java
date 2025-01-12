package exception;

// TODO: Finalize documentation
/**
 * Thrown when ...
 * For example, ...
 *
 * @since   1.0
 */
public class TermSyntaxException extends RuntimeException {
    /**
     * Constructs an {@code TermSyntaxException} with no detail
     * message.
     */
    public TermSyntaxException() {
        super();
    }
    
    /**
     * Constructs an {@code TermSyntaxException} with the specified
     * detail message.
     *
     * @param   s   the detail message.
     */
    public TermSyntaxException(String s) {
        super(s);
    }
}
