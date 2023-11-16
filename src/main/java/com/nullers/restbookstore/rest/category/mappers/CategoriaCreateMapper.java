package com.nullers.restbookstore.rest.category.mappers;


import com.nullers.restbookstore.rest.category.dto.CategoriaCreateDto;
import com.nullers.restbookstore.rest.category.models.Categoria;

import java.util.UUID;

public class CategoriaCreateMapper {

    public static Categoria toEntity(CategoriaCreateDto dto){
        return Categoria.builder()
                .id(UUID.randomUUID())
                .nombre(dto.getNombre())
                .activa(dto.isActiva())
                .build();
    }

    public static Categoria toEntity(CategoriaCreateDto dto, Categoria entity){
        return Categoria.builder()
                .id(entity.getId() == null ? UUID.randomUUID() : entity.getId())
                .nombre(dto.getNombre() == null ? entity.getNombre() : dto.getNombre())
                .activa(dto.isActiva() == entity.isActiva() ? entity.isActiva() : dto.isActiva())
                .fecha_creacion(entity.getFecha_creacion())
                .build();
    }

    public static CategoriaCreateDto toDto(Categoria entity){
        return CategoriaCreateDto.builder()
                .nombre(entity.getNombre())
                .activa(entity.isActiva())
                .build();
    }

}
