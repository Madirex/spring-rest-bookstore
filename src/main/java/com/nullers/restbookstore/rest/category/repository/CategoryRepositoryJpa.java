package com.nullers.restbookstore.rest.category.repository;

import com.nullers.restbookstore.rest.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Interfaz CategoryRepositoryJpa
 */
@Repository
public interface CategoryRepositoryJpa extends JpaRepository<Category, UUID>, JpaSpecificationExecutor<Category> {

    /**
     * Método para obtener una categoría por su nombre
     *
     * @param name nombre de la categoría
     * @return categoría
     */
    Optional<Category> findByName(String name);
}
