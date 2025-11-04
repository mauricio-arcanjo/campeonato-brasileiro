package com.mauarcanjo.campeonato_brasileiro.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorDetails {

    private String message;
    private String details;
    private String errorCode;
    private LocalDateTime timestamp;

}