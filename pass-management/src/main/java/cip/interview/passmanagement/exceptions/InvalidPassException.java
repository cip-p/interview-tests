package cip.interview.passmanagement.exceptions;

public class InvalidPassException extends RuntimeException {

    public InvalidPassException(String message) {
        super(message);
    }
}
