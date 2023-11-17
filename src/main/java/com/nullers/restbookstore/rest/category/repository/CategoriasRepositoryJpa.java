package com.nullers.restbookstore.rest.category.repository;

import com.nullers.restbookstore.rest.category.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoriasRepositoryJpa extends JpaRepository<Categoria, UUID>, JpaSpecificationExecutor<Categoria> {

    public Optional<Categoria> findById(UUID id);

    public Optional<Categoria> findByNombre(String nombre);
}
