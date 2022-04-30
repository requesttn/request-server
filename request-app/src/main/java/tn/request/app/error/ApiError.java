package tn.request.app.error;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

public class ApiError {

    @JsonIgnore
    private HttpStatus errorCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    ApiError(HttpStatus errorCode) {
        this();
        this.errorCode = errorCode;
    }

    public ApiError(HttpStatus errorCode, Throwable ex) {
        this();
        this.errorCode = errorCode;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    ApiError(HttpStatus errorCode, String message, Throwable ex) {
        this();
        this.errorCode = errorCode;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }
}