package org.vinhpham.sticket.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.vinhpham.sticket.dtos.Failure;
import org.vinhpham.sticket.dtos.HandleException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler({HandleException.class})
    public ResponseEntity<?> handleException(final HandleException ex) {
        return Failure.response(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler({Throwable.class})
    public ResponseEntity<?> handleUnexpectedException(final Throwable throwable) {
        throwable.printStackTrace();
        return Failure.internal(messageSource, "error.something.wrong");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });

        String message = String.join("\n", errors);
        return Failure.bad(message);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<?> handleNoHandlerFoundException(final HttpRequestMethodNotSupportedException ex) {
        ex.printStackTrace();
        return Failure.response(messageSource, "error.not.found", HttpStatus.NOT_FOUND);
    }
}
