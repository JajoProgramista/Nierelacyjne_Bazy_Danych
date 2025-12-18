package GameOfLifeExceptions;

import java.util.ResourceBundle;

public class NullException extends RuntimeException {
    private final String messageKey;

    public NullException(String messageKey) {
        super(messageKey);
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
