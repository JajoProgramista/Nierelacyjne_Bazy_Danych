package GameOfLifeExceptions;

import java.util.ResourceBundle;

public class InvalidBoardSizeException extends RuntimeException {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("exceptions");
    private final String messageKey;

    public InvalidBoardSizeException(String messageKey) {
        super(bundle.getString(messageKey));
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
