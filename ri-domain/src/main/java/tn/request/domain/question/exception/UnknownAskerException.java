package tn.request.domain.question.exception;

public class UnknownAskerException extends RuntimeException {
    public UnknownAskerException(String message) {
        super(message);
    }

    public UnknownAskerException() {
        super();
    }
}
