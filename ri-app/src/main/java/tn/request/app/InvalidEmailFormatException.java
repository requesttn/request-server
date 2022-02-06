package tn.request.app;

import lombok.experimental.StandardException;

public class InvalidEmailFormatException extends Exception {
    public InvalidEmailFormatException(String message) {
        super(message);
    }
}
