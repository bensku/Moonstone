package io.github.bensku.call;

/**
 * Thrown when a resolver cannot get/set value of field, at all.
 * If this happens, you should probably try NullResolver, which always
 * returns nulls and never throws this.
 *
 */
public class NotResolvableException extends Exception {

    public NotResolvableException(String message) {
        super(message);
    }

    private static final long serialVersionUID = -6369295138952193389L;
    
    // Ignore stack trace, as it improves performance
    public Throwable fillInStackTrace() {
        return this;
        
    }

}
