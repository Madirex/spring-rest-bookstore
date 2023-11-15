package com.nullers.restbookstore.rest.category.mappers;

import com.nullers.restbookstore.rest.category.dto.CategoriaCreateDto;
import com.nullers.restbookstore.rest.category.mappers.CategoriaCreateMapper;
import com.nullers.restbookstore.rest.category.models.Categoria;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CategoriasMapperTest {

    Categoria categoria2 = Categoria.builder()
            .id(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))
            .nombre("categoria 2")
            .build();

    CategoriaCreateDto categoriaCreateDto = CategoriaCreateDto.builder()
            .nombre("categoria 1")
            .activa(true)
            .build();

    @Test
    void toEntity() {
        Categoria categoria = CategoriaCreateMapper.toEntity(categoriaCreateDto);

        assertAll(
                () -> assertEquals(categoriaCreateDto.getNombre(), categoria.getNombre()),
                () -> assertEquals(categoriaCreateDto.isActiva(), categoria.isActiva())
        );
    }

    @Test
    void toEntity2() {
        Categoria categoria = CategoriaCreateMapper.toEntity(categoriaCreateDto, categoria2);

        assertAll(
                () -> assertEquals(categoriaCreateDto.getNombre(), categoria.getNombre()),
                () -> assertEquals(categoriaCreateDto.isActiva(), categoria.isActiva()),
                () -> assertEquals(categoria2.getId(), categoria.getId()),
                () -> assertEquals(categoria2.getFecha_creacion(), categoria.getFecha_creacion())
        );
    }

    @Test
    void toDto() {
        CategoriaCreateDto categoriaCreateDto = CategoriaCreateMapper.toDto(categoria2);

        assertAll(
                () -> assertEquals(categoria2.getNombre(), categoriaCreateDto.getNombre()),
                () -> assertEquals(categoria2.isActiva(), categoriaCreateDto.isActiva())
        );
    }

}
