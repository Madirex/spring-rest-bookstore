package com.nullers.restbookstore.rest.category.mappers;


import com.nullers.restbookstore.rest.category.dto.CategoryCreateDTO;
import com.nullers.restbookstore.rest.category.model.Category;

import java.util.UUID;

/**
 * Clase CategoryCreateMapper
 */
public class CategoryCreateMapper {

    /**
     * Constructor privado para evitar instancias
     */
    private CategoryCreateMapper() {
        // Constructor privado para evitar instancias
    }

    /**
     * Método para convertir un DTO en una entidad
     *
     * @param dto DTO
     * @return entidad
     */
    public static Category toEntity(CategoryCreateDTO dto) {
        return Category.builder()
                .id(UUID.randomUUID())
                .name(dto.getName())
                .isActive(dto.isActive())
                .build();
    }

    /**
     * Método para convertir un DTO en una entidad
     *
     * @param dto    DTO
     * @param entity entidad
     * @return entidad
     */
    public static Category toEntity(CategoryCreateDTO dto, Category entity) {
        return Category.builder()
                .id(entity.getId() == null ? UUID.randomUUID() : entity.getId())
                .name(dto.getName() == null ? entity.getName() : dto.getName())
                .isActive(dto.isActive() == entity.getIsActive() ? entity.getIsActive() : dto.isActive())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    /**
     * Método para convertir una entidad en un DTO
     *
     * @param entity entidad
     * @return DTO
     */
    public static CategoryCreateDTO toDto(Category entity) {
        return CategoryCreateDTO.builder()
                .name(entity.getName())
                .isActive(entity.getIsActive())
                .build();
    }

}
