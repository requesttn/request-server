package tn.request.questions;

public class UnknownAskerException extends RuntimeException {
    public UnknownAskerException(String message) {
        super(message);
    }

    public UnknownAskerException() {
        super();
    }
}
