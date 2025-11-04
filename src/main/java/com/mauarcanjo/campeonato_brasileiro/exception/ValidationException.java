package com.mauarcanjo.campeonato_brasileiro.exception;

public class ValidationException extends RuntimeException{
    public ValidationException(String message) {
        super(message);
    }
}
