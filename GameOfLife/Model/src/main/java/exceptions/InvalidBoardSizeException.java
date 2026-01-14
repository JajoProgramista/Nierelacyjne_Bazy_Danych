package exceptions;

public class InvalidBoardSizeException extends RuntimeException {
    private final String messageKey;

    public InvalidBoardSizeException(String messageKey) {
        super(messageKey);
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
