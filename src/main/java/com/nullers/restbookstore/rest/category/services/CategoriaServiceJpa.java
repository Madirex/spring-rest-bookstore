package com.nullers.restbookstore.rest.category.services;


import com.nullers.restbookstore.rest.category.dto.CategoriaCreateDto;
import com.nullers.restbookstore.rest.category.models.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CategoriaServiceJpa {

    Page<Categoria> getAll(Optional<String> nombre, Optional<Boolean> activa, Pageable pageable);

    Categoria getCategoriaById(UUID id);

    Categoria updateCategoria(UUID id, CategoriaCreateDto categoriaCreateDto);

    void deleteById(UUID categoria);

    Categoria getCategoriaByNombre(String nombre);

    Categoria createCategoria(CategoriaCreateDto categoriaCreateDto);

}
