package com.nullers.restbookstore.rest.category.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Class CategoryNotFoundException
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends RuntimeException {

    /**
     * Constructor CategoryNotFoundException
     *
     * @param id Id de la categoría
     */
    public CategoryNotFoundException(UUID id) {
        super("Category con id " + id + " no encontrada");
    }

    /**
     * Constructor CategoryNotFoundException
     *
     * @param name Nombre de la categoría
     */
    public CategoryNotFoundException(String name) {
        super("Category con nombre " + name + " no encontrada");
    }
}
