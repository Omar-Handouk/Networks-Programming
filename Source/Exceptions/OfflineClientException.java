package Exceptions;

public class OfflineClientException extends RuntimeException{

    public OfflineClientException() {
    }

    public OfflineClientException(String message) {
        super(message);
    }
}
