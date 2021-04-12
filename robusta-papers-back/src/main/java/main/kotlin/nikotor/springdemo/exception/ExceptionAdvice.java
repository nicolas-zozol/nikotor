package main.kotlin.nikotor.springdemo.exception;

import io.robusta.nikotor.NikotorException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NikotorException.class})
    protected ResponseEntity<Object> handleEngineException(RuntimeException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();

        if (ex instanceof NikotorException) {
            NikotorException exception = (NikotorException) ex;
            return handleExceptionInternal(exception, exception.getReason().getMessage(),
                    headers, HttpStatus.valueOf(exception.getReason().getStatus()), request);
        }

        // Default case
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(ex, null, headers, status, request);
    }
}
