package com.nullers.restbookstore.rest.category.exceptions;

import java.util.UUID;

public class CategoriaNotFoundException extends CategoriaException{

            public CategoriaNotFoundException(UUID id) {
                super("Categoria con id " + id + " no encontrada");
            }

            public CategoriaNotFoundException(String nombre) {
                super("Categoria con nombre " + nombre + " no encontrada");
            }
}