package com.nullers.restbookstore.rest.category.services;


import com.nullers.restbookstore.rest.category.dto.CategoryCreateDTO;
import com.nullers.restbookstore.rest.category.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Interfaz CategoryServiceJpa
 */
public interface CategoryServiceJpa {

    /**
     * Método para obtener todas las categorías
     *
     * @param name     nombre por el que filtrar
     * @param isActive activa o no
     * @param pageable paginación
     * @return categorías
     */
    Page<Category> getAll(Optional<String> name, Optional<Boolean> isActive, Pageable pageable);

    /**
     * Método para obtener una categoría por su id
     *
     * @param id id de la categoría
     * @return categoría
     */
    Category getCategoryById(UUID id);

    /**
     * Método para actualizar una categoría por su id
     *
     * @param id                id de la categoría
     * @param categoryCreateDTO DTO de categoría
     * @return categoría
     */
    Category updateCategory(UUID id, CategoryCreateDTO categoryCreateDTO);

    /**
     * Método para eliminar una categoría por su id
     *
     * @param category id de la categoría
     */
    void deleteById(UUID category);

    /**
     * Método para obtener una categoría por su nombre
     *
     * @param name nombre de la categoría
     * @return categoría
     */
    Category getCategoryByName(String name);

    /**
     * Método para crear una categoría
     *
     * @param categoryCreateDTO DTO de categoría
     * @return categoría
     */
    Category createCategory(CategoryCreateDTO categoryCreateDTO);

}
