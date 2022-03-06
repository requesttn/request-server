package tn.request.questions;

public class NonVerifiedAskerException extends RuntimeException {
    public NonVerifiedAskerException(String message) {
        super(message);
    }

    public NonVerifiedAskerException() {
        super();
    }
}
