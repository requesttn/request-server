package tn.request.domain.question.exception;

public class NonVerifiedAskerException extends RuntimeException {
    public NonVerifiedAskerException(String message) {
        super(message);
    }

    public NonVerifiedAskerException() {
        super();
    }
}
