package tn.request.service.question.exception;

public class UnknownAskerException extends RuntimeException {
    public UnknownAskerException(String message) {
        super(message);
    }

    public UnknownAskerException() {
        super();
    }
}
