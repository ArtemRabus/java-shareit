package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler(ValidateException.class)
    public ResponseEntity<Response> handleException(ValidateException e) {
        log.info("Error 400: {}", e.getMessage(), e.getCause());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response> handleException(NotFoundException e) {
        log.info("Error 404: {}", e.getMessage(), e.getCause());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response> handleException(RuntimeException e) {
        log.info("Error 500: {}", e.getMessage(), e.getCause());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MessageFailedException.class)
    public ResponseEntity<Map<String, String>> handleException(MessageFailedException e) {
        log.info("Error 400: {}", e.getMessage(), e.getCause());
        return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
