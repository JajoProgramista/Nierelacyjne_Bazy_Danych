package GameOfLifeExceptions;

import java.util.ResourceBundle;

public class NoneNeigboursException extends RuntimeException {
    private final String messageKey;

    public NoneNeigboursException(String messageKey) {
        super(messageKey);
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
