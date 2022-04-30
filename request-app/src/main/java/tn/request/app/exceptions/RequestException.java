package tn.request.app.exceptions;

import org.springframework.http.HttpStatus;

/**
 * An exception thrown by the request app.
 */
public class RequestException extends RuntimeException {

    private final HttpStatus errorCode;

    public RequestException(HttpStatus errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public RequestException(HttpStatus errorCode, String message, String... params) {
        this(errorCode, String.format(message, params));
    }

    public RequestException(String message, String... params) {
        this(HttpStatus.BAD_REQUEST, message, params);
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }
}
