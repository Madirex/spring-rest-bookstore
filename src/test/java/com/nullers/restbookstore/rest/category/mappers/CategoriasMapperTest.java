package com.nullers.restbookstore.rest.category.mappers;

import com.nullers.restbookstore.rest.category.dto.CategoryCreateDTO;
import com.nullers.restbookstore.rest.category.model.Category;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CategoriasMapperTest {

    Category category2 = Category.builder()
            .id(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))
            .name("categoria 2")
            .build();

    CategoryCreateDTO categoryCreateDTO = CategoryCreateDTO.builder()
            .name("categoria 1")
            .isActive(true)
            .build();

    @Test
    void toEntity() {
        Category category = CategoryCreateMapper.toEntity(categoryCreateDTO);

        assertAll(
                () -> assertEquals(categoryCreateDTO.getName(), category.getName()),
                () -> assertEquals(categoryCreateDTO.isActive(), category.getIsActive())
        );
    }

    @Test
    void toEntity2() {
        Category category = CategoryCreateMapper.toEntity(categoryCreateDTO, category2);

        assertAll(
                () -> assertEquals(categoryCreateDTO.getName(), category.getName()),
                () -> assertEquals(categoryCreateDTO.isActive(), category.getIsActive()),
                () -> assertEquals(category2.getId(), category.getId()),
                () -> assertEquals(category2.getCreatedAt(), category.getCreatedAt())
        );
    }

    @Test
    void toDto() {
        CategoryCreateDTO categoryCreateDTO = CategoryCreateMapper.toDto(category2);

        assertAll(
                () -> assertEquals(category2.getName(), categoryCreateDTO.getName()),
                () -> assertEquals(category2.getIsActive(), categoryCreateDTO.isActive())
        );
    }

}
