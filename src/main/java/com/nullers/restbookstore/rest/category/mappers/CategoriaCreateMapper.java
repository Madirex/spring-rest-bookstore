package com.nullers.restbookstore.rest.category.mappers;


import com.nullers.restbookstore.rest.category.dto.CategoriaCreateDto;
import com.nullers.restbookstore.rest.category.model.Category;

import java.util.UUID;

public class CategoriaCreateMapper {

    public static Category toEntity(CategoriaCreateDto dto) {
        return Category.builder()
                .id(UUID.randomUUID())
                .name(dto.getName())
                .isActive(dto.isActive())
                .build();
    }

    public static Category toEntity(CategoriaCreateDto dto, Category entity) {
        return Category.builder()
                .id(entity.getId() == null ? UUID.randomUUID() : entity.getId())
                .name(dto.getName() == null ? entity.getName() : dto.getName())
                .isActive(dto.isActive() == entity.getIsActive() ? entity.getIsActive() : dto.isActive())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static CategoriaCreateDto toDto(Category entity) {
        return CategoriaCreateDto.builder()
                .name(entity.getName())
                .isActive(entity.getIsActive())
                .build();
    }

}
