package exceptions;

public class DataBaseException extends RuntimeException {

    public DataBaseException(String messageKey) {
        super(messageKey);
    }

}
