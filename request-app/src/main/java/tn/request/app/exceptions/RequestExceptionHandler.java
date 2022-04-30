package tn.request.app.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tn.request.app.error.ApiError;

@ControllerAdvice
public class RequestExceptionHandler {

    @ExceptionHandler({RequestException.class})
    protected ResponseEntity<ApiError> handleRequestEntityException(RequestException exception) {
        return buildResponseEntity(new ApiError(exception.getErrorCode(), exception));
    }

    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getErrorCode());
    }
}
