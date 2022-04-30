package tn.request.app.error;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter
@Setter
public class ApiError {

    @JsonIgnore
    private HttpStatus errorCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;

    public ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus errorCode) {
        this();
        this.errorCode = errorCode;
    }

    public ApiError(HttpStatus errorCode, Throwable ex) {
        this();
        this.errorCode = errorCode;
        this.message = ex.getMessage();
    }

    public ApiError(HttpStatus errorCode, String message, Throwable ex) {
        this();
        this.errorCode = errorCode;
        this.message = message;
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }
}