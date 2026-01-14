package exceptions;

public class NoneLineException extends RuntimeException {
    public NoneLineException(String messageKey) {
        super(messageKey);
    }

}
