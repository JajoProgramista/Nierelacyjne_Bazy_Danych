package exceptions;

public class FileException extends RuntimeException {

    public FileException(String messageKey) {
        super(messageKey);
    }

}
