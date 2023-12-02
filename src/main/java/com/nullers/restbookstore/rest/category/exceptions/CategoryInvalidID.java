package com.nullers.restbookstore.rest.category.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class CategoryInvalidID
 *
 * @author Madirex
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CategoryInvalidID extends CategoriaException {
    /**
     * Constructor CategoryInvalidID
     *
     * @param id Id de la categoría
     */
    public CategoryInvalidID(String id) {
        super("ID de categoría:" + id + " no válido.");
    }
}
