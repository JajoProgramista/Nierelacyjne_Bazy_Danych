package GameOfLifeExceptions;

public class FileException extends RuntimeException {
    private final String messageKey;

    public FileException(String messageKey) {
        super(messageKey);
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
