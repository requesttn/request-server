package tn.request.app.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tn.request.app.error.ApiError;
import tn.request.service.question.exception.RequestException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({RequestException.class})
    protected ResponseEntity<ApiError> handleRequestEntityException(RequestException exception) {
        log.error(exception.getMessage());
        return buildResponseEntity(new ApiError(exception.getErrorCode(), exception));
    }

    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getErrorCode());
    }
}
