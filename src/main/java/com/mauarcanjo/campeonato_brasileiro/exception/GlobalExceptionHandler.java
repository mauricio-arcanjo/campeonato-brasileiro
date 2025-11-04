package com.mauarcanjo.campeonato_brasileiro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLException;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorDetails> handleApiException(ApiException exception,
                                                           WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(
                exception.getMessage(),
                "BAD_REQUEST",
                webRequest.getDescription(false),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorDetails> handleValidationException(ValidationException exception,
                                                           WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(
                exception.getMessage(),
                "BAD_REQUEST",
                webRequest.getDescription(false),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorDetails> handleApiException(SQLException exception,
                                                           WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(
                exception.getMessage(),
                "BAD_REQUEST",
                webRequest.getDescription(false),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
