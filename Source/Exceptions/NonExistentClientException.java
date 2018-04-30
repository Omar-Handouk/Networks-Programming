package Exceptions;

public class NonExistentClientException extends RuntimeException{

    public NonExistentClientException() {
    }

    public NonExistentClientException(String message) {
        super(message);
    }
}
