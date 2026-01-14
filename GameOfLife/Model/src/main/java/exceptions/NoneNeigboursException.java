package exceptions;

public class NoneNeigboursException extends RuntimeException {

    public NoneNeigboursException(String messageKey) {
        super(messageKey);
    }

}
