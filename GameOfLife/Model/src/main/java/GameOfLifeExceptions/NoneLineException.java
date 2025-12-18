package GameOfLifeExceptions;

import java.util.ResourceBundle;

public class NoneLineException extends RuntimeException{
    private final String messageKey;

    public NoneLineException(String messageKey) {
        super(messageKey);
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
