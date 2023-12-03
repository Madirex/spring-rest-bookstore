package com.nullers.restbookstore.rest.publisher.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Clase PublisherNotFound
 *
 * @author jaimesalcedo1
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PublisherNotFound extends PublisherException {

    /**
     * Constructor PublisherNotFound
     *
     * @param message Mensaje de error
     */
    public PublisherNotFound(String message) {
        super("Editorial no encontrada: " + message);
    }
}
