package tn.request.authentication;

public class InvalidConfirmationTokenException extends RuntimeException {
    public InvalidConfirmationTokenException(String message) {
        super(message);
    }
}
