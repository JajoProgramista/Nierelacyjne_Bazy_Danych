package GameOfLifeExceptions;

import java.util.ResourceBundle;

public class NoneLineException extends RuntimeException{
    private static final ResourceBundle bundle = ResourceBundle.getBundle("exceptions");
    private final String messageKey;

    public NoneLineException(String messageKey) {
        super(bundle.getString(messageKey));
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
