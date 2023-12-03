package com.nullers.restbookstore.rest.category.exceptions;

import java.util.UUID;

public class CategoryNotFoundException extends CategoryException {

            public CategoryNotFoundException(UUID id) {
                super("Categoria con id " + id + " no encontrada");
            }

            public CategoryNotFoundException(String name) {
                super("Categoria con nombre " + name + " no encontrada");
            }
}
