package com.nullers.restbookstore.manager.error.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Clase ErrorResponse
 *
 * @Author Madirex
 */
@Getter
public class ErrorResponse {

    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    /**
     * Constructor ErrorResponse
     *
     * @param httpStatus HttpStatus
     * @param errorMsg   Mensaje de error
     * @param path       Ruta
     */
    public ErrorResponse(HttpStatus httpStatus, String errorMsg, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = httpStatus.value();
        this.error = errorMsg;
        this.message = httpStatus.name();
        this.path = path;
    }
}