package com.nullers.restbookstore.rest.publisher.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Clase PublisherIDNotValid
 *
 * @author jaimesalcedo1
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PublisherIDNotValid extends PublisherException {

    /**
     * Constructor PublisherIDNotValid
     *
     * @param message Mensaje de error
     */
    public PublisherIDNotValid(String message) {
        super("El ID no es válido: " + message);
    }
}
