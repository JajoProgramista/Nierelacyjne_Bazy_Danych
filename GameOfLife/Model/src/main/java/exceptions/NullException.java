package exceptions;

public class NullException extends RuntimeException {

    public NullException(String messageKey) {
        super(messageKey);
    }
}
