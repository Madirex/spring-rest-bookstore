package com.nullers.restbookstore.publisher.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PublisherUUIDNotValid extends PublisherException{

    public PublisherUUIDNotValid(String message) {
        super("El UUID no es válido: " + message);
    }
}
