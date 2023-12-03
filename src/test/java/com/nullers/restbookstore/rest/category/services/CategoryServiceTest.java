package com.nullers.restbookstore.rest.category.services;


import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.repository.BookRepository;
import com.nullers.restbookstore.rest.category.exceptions.CategoryConflictException;
import com.nullers.restbookstore.rest.category.exceptions.CategoryNotFoundException;
import com.nullers.restbookstore.rest.category.mappers.CategoryCreateMapper;
import com.nullers.restbookstore.rest.category.model.Category;
import com.nullers.restbookstore.rest.category.repository.CategoryRepositoryJpa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepositoryJpa repository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private CategoryServiceJpaImpl service;

    Category category1 = Category.builder()
            .id(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))
            .name("category 1")
            .isActive(true)
            .build();

    Category category2 = Category.builder()
            .id(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9735"))
            .name("category 2")
            .isActive(true)
            .build();

    @Test
    void getAll_ShoulReturnAllBooksWithoutParamsPageable() {
        List<Category> expectedCategories = List.of(category1, category2);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        Page<Category> expectedPage = new PageImpl(expectedCategories);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Category> result = service.getAll(Optional.empty(), Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(expectedPage.getTotalPages(), result.getTotalPages()),
                () -> assertEquals(expectedPage.getTotalElements(), result.getTotalElements()),
                () -> assertEquals(expectedPage.getNumber(), result.getNumber()),
                () -> assertEquals(expectedPage.getNumberOfElements(), result.getNumberOfElements()),
                () -> assertEquals(expectedPage.getSize(), result.getSize()),
                () -> assertEquals(expectedPage.getContent().get(0).getName(), result.getContent().get(0).getName()),
                () -> assertEquals(expectedPage.getContent().get(0).getIsActive(), result.getContent().get(0).getIsActive()),
                () -> assertEquals(expectedPage.getContent().get(1).getName(), result.getContent().get(1).getName()),
                () -> assertEquals(expectedPage.getContent().get(1).getIsActive(), result.getContent().get(1).getIsActive())
        );

    }

    @Test
    void getAll_ShoulReturnAllBooksNameParamPageable() {
        List<Category> expectedCategories = List.of(category1);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        Page<Category> expectedPage = new PageImpl(expectedCategories);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Category> result = service.getAll(Optional.of("category 1"), Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(expectedPage.getTotalPages(), result.getTotalPages()),
                () -> assertEquals(expectedPage.getTotalElements(), result.getTotalElements()),
                () -> assertEquals(expectedPage.getNumber(), result.getNumber()),
                () -> assertEquals(expectedPage.getNumberOfElements(), result.getNumberOfElements()),
                () -> assertEquals(expectedPage.getSize(), result.getSize()),
                () -> assertEquals(category1.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(category1.getIsActive(), result.getContent().get(0).getIsActive())
        );
    }

    @Test
    void getAll_ShoulReturnAllBooksIsActiveParamPageable() {
        List<Category> expectedCategories = List.of(category1);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        Page<Category> expectedPage = new PageImpl(expectedCategories);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Category> result = service.getAll(Optional.empty(), Optional.of(true), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(expectedPage.getTotalPages(), result.getTotalPages()),
                () -> assertEquals(expectedPage.getTotalElements(), result.getTotalElements()),
                () -> assertEquals(expectedPage.getNumber(), result.getNumber()),
                () -> assertEquals(expectedPage.getNumberOfElements(), result.getNumberOfElements()),
                () -> assertEquals(expectedPage.getSize(), result.getSize()),
                () -> assertEquals(category1.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(category1.getIsActive(), result.getContent().get(0).getIsActive())
        );
    }

    @Test
    void getAll_ShoulReturnAllBooksIsActiveFalseParamPageable() {
        List<Category> expectedCategories = List.of();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        Page<Category> expectedPage = new PageImpl(expectedCategories);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Category> result = service.getAll(Optional.empty(), Optional.of(false), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(expectedPage.getTotalPages(), result.getTotalPages()),
                () -> assertEquals(expectedPage.getTotalElements(), result.getTotalElements()),
                () -> assertEquals(expectedPage.getNumber(), result.getNumber()),
                () -> assertEquals(expectedPage.getNumberOfElements(), result.getNumberOfElements()),
                () -> assertEquals(expectedPage.getSize(), result.getSize())
        );
    }

    @Test
    void getCategoryById() {
        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))).thenReturn(Optional.of(category1));

        var category = service.getCategoryById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));

        assertAll(
                () -> assertNotNull(category),
                () -> assertEquals(category1.getName(), category.getName()),
                () -> assertEquals(category1.getIsActive(), category.getIsActive())
        );
    }

    @Test
    void getCategoryByIdNotFound() {
        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466f99999"))).thenReturn(Optional.empty());

        var res = assertThrows(CategoryNotFoundException.class, () -> service.getCategoryById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466f99999")));
        assertEquals("Category con id 3930e05a-7ebf-4aa1-8aa8-5d7466f99999 no encontrada", res.getMessage());
    }

    @Test
    void getCategoryByName() {
        when(repository.findByName("category 1")).thenReturn(Optional.of(category1));

        var category = service.getCategoryByName("category 1");

        assertAll(
                () -> assertNotNull(category),
                () -> assertEquals(category1.getName(), category.getName()),
                () -> assertEquals(category1.getIsActive(), category.getIsActive())
        );
    }

    @Test
    void getCategoryByNameNotFound() {
        when(repository.findByName("category 99")).thenReturn(Optional.empty());

        var res = assertThrows(CategoryNotFoundException.class, () -> service.getCategoryByName("category 99"));
        assertEquals("Category con nombre category 99 no encontrada", res.getMessage());
    }

    @Test
    void createCategory() {
        when(repository.save(any(Category.class))).thenReturn(category1);
        when(repository.findByName("category 1")).thenReturn(Optional.empty());

        var category = service.createCategory(CategoryCreateMapper.toDto(category1));

        assertAll(
                () -> assertNotNull(category),
                () -> assertEquals(category1.getName(), category.getName()),
                () -> assertEquals(category1.getIsActive(), category.getIsActive())
        );
    }

    @Test
    void createCategoryConflict() {
        when(repository.findByName("category 1")).thenReturn(Optional.of(category1));

        var res = assertThrows(CategoryConflictException.class, () -> service.createCategory(CategoryCreateMapper.toDto(category1)));
        assertEquals("Ya existe una categoría con el nombre: category 1", res.getMessage());
    }

    @Test
    void updateCategory() {
        when(repository.save(any(Category.class))).thenReturn(category1);
        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))).thenReturn(Optional.of(category1));
        when(repository.findByName("category 1")).thenReturn(Optional.empty());

        var category = service.updateCategory(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"), CategoryCreateMapper.toDto(category1));

        assertAll(
                () -> assertNotNull(category),
                () -> assertEquals(category1.getName(), category.getName()),
                () -> assertEquals(category1.getIsActive(), category.getIsActive())
        );
    }

    @Test
    void updateCategoryNotFound() {
        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-999966fa9734"))).thenReturn(Optional.empty());

        var res = assertThrows(CategoryNotFoundException.class, () -> service.updateCategory(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-999966fa9734"), CategoryCreateMapper.toDto(category1)));
        assertEquals("Category con id 3930e05a-7ebf-4aa1-8aa8-999966fa9734 no encontrada", res.getMessage());
    }

    @Test
    void updateCategoryConflict() {
        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))).thenReturn(Optional.of(category1));
        when(repository.findByName("category 1")).thenReturn(Optional.of(category2));

        var res = assertThrows(CategoryConflictException.class, () -> service.updateCategory(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"), CategoryCreateMapper.toDto(category1)));
        assertEquals("Ya existe una categoría con el nombre: category 1", res.getMessage());
    }

    @Test
    void deleteCategoryWithBooks() {
        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))).thenReturn(Optional.of(category1));
        when(bookRepository.findByCategory_Name(any())).thenReturn(new ArrayList<>());
        doThrow(new CategoryConflictException("No se puede eliminar la categoría porque tiene libros asociados")).when(repository).deleteById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));

        var res = assertThrows(CategoryConflictException.class, () -> service.deleteById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")));
        assertEquals("No se puede eliminar la categoría porque tiene libros asociados", res.getMessage());
    }


    @Test
    void deleteCategoryNotFound() {
        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-999966fa9734"))).thenReturn(Optional.empty());

        var res = assertThrows(CategoryNotFoundException.class, () -> service.deleteById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-999966fa9734")));
        assertEquals("Category con id 3930e05a-7ebf-4aa1-8aa8-999966fa9734 no encontrada", res.getMessage());
    }

    @Test
    void deleteCategory() {
        List<GetBookDTO> expectedbooks = List.of();

        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))).thenReturn(Optional.of(category1));
        when(bookRepository.findByCategory_Name(any())).thenReturn(new ArrayList<>());

        service.deleteById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));
    }


}
