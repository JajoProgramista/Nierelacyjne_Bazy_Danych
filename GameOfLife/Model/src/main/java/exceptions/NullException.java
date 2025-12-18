package exceptions;

import java.util.ResourceBundle;

public class NullException extends RuntimeException {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("exceptions");
    private final String messageKey;

    public NullException(String messageKey) {
        super(bundle.getString(messageKey));
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
