package com.nullers.restbookstore.rest.category.repository;

import com.nullers.restbookstore.rest.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoriasRepositoryJpa extends JpaRepository<Category, UUID>, JpaSpecificationExecutor<Category> {

    public Optional<Category> findById(UUID id);

    public Optional<Category> findByNombre(String nombre);
}
