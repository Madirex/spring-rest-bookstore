package com.nullers.restbookstore.manager.error.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Interfaz ResponseExceptionInterface
 * Esta interfaz define el método getHttpStatus que deben implementar las clases
 *
 * @author Madirex
 */
public interface ResponseExceptionInterface {
    /**
     * Devuelve el HttpStatus
     *
     * @return HttpStatus
     */
    HttpStatus getHttpStatus();
}
