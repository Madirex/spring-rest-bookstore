package com.nullers.restbookstore.publisher.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Clase PublisherNotFound
 *
 * @author jaimesalcedo1
 * */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PublisherNotFound extends PublisherException{

    public PublisherNotFound(String message) {
        super("Editioral no encontrada: " + message);
    }
}
