package Exceptions;

public class WrongFormatException extends RuntimeException{

    public WrongFormatException() {
    }

    public WrongFormatException(String message) {
        super(message);
    }
}
