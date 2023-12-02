package com.nullers.restbookstore.rest.category.services;


import com.nullers.restbookstore.rest.category.dto.CategoriaCreateDto;
import com.nullers.restbookstore.rest.category.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CategoriaServiceJpa {

    Page<Category> getAll(Optional<String> nombre, Optional<Boolean> activa, Pageable pageable);

    Category getCategoriaById(UUID id);

    Category updateCategoria(UUID id, CategoriaCreateDto categoriaCreateDto);

    void deleteById(UUID categoria);

    Category getCategoriaByNombre(String nombre);

    Category createCategoria(CategoriaCreateDto categoriaCreateDto);

}
