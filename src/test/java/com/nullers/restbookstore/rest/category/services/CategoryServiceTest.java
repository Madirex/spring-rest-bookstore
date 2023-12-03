package com.nullers.restbookstore.rest.category.services;


import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.repository.BookRepository;
import com.nullers.restbookstore.rest.category.exceptions.CategoriaConflictException;
import com.nullers.restbookstore.rest.category.exceptions.CategoriaNotFoundException;
import com.nullers.restbookstore.rest.category.mappers.CategoriaCreateMapper;
import com.nullers.restbookstore.rest.category.model.Category;
import com.nullers.restbookstore.rest.category.repository.CategoriasRepositoryJpa;
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
    private CategoriasRepositoryJpa repository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private CategoriaServiceJpaImpl service;

    Category category1 = Category.builder()
            .id(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))
            .name("categoria 1")
            .isActive(true)
            .build();

    Category category2 = Category.builder()
            .id(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9735"))
            .name("categoria 2")
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

        Page<Category> result = service.getAll(Optional.of("categoria 1"), Optional.empty(), pageable);

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
    void getAll_ShoulReturnAllBooksActivaParamPageable() {
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
    void getAll_ShoulReturnAllBooksActivaFalseParamPageable() {
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
    void getCategoriaById() {
        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))).thenReturn(Optional.of(category1));

        var categoria = service.getCategoriaById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));

        assertAll(
                () -> assertNotNull(categoria),
                () -> assertEquals(category1.getName(), categoria.getName()),
                () -> assertEquals(category1.getIsActive(), categoria.getIsActive())
        );
    }

    @Test
    void getCategoriaByIdNotFound() {
        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466f99999"))).thenReturn(Optional.empty());

        var res = assertThrows(CategoriaNotFoundException.class, () -> service.getCategoriaById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466f99999")));
        assertEquals("Categoria con id 3930e05a-7ebf-4aa1-8aa8-5d7466f99999 no encontrada", res.getMessage());
    }

    @Test
    void getCategoriaByName() {
        when(repository.findByName("categoria 1")).thenReturn(Optional.of(category1));

        var categoria = service.getCategoriaByName("categoria 1");

        assertAll(
                () -> assertNotNull(categoria),
                () -> assertEquals(category1.getName(), categoria.getName()),
                () -> assertEquals(category1.getIsActive(), categoria.getIsActive())
        );
    }

    @Test
    void getCategoriaByNameNotFound() {
        when(repository.findByName("categoria 99")).thenReturn(Optional.empty());

        var res = assertThrows(CategoriaNotFoundException.class, () -> service.getCategoriaByName("categoria 99"));
        assertEquals("Categoria con nombre categoria 99 no encontrada", res.getMessage());
    }

    @Test
    void createCategoria() {
        when(repository.save(any(Category.class))).thenReturn(category1);
        when(repository.findByName("categoria 1")).thenReturn(Optional.empty());

        var categoria = service.createCategoria(CategoriaCreateMapper.toDto(category1));

        assertAll(
                () -> assertNotNull(categoria),
                () -> assertEquals(category1.getName(), categoria.getName()),
                () -> assertEquals(category1.getIsActive(), categoria.getIsActive())
        );
    }

    @Test
    void createCategoriaConflict() {
        when(repository.findByName("categoria 1")).thenReturn(Optional.of(category1));

        var res = assertThrows(CategoriaConflictException.class, () -> service.createCategoria(CategoriaCreateMapper.toDto(category1)));
        assertEquals("Ya existe una categoria con el nombre: categoria 1", res.getMessage());
    }

    @Test
    void updateCategoria() {
        when(repository.save(any(Category.class))).thenReturn(category1);
        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))).thenReturn(Optional.of(category1));
        when(repository.findByName("categoria 1")).thenReturn(Optional.empty());

        var categoria = service.updateCategoria(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"), CategoriaCreateMapper.toDto(category1));

        assertAll(
                () -> assertNotNull(categoria),
                () -> assertEquals(category1.getName(), categoria.getName()),
                () -> assertEquals(category1.getIsActive(), categoria.getIsActive())
        );
    }

    @Test
    void updateCategoriaNotFound() {
        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-999966fa9734"))).thenReturn(Optional.empty());

        var res = assertThrows(CategoriaNotFoundException.class, () -> service.updateCategoria(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-999966fa9734"), CategoriaCreateMapper.toDto(category1)));
        assertEquals("Categoria con id 3930e05a-7ebf-4aa1-8aa8-999966fa9734 no encontrada", res.getMessage());
    }

    @Test
    void updateCategoriaConflict() {
        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))).thenReturn(Optional.of(category1));
        when(repository.findByName("categoria 1")).thenReturn(Optional.of(category2));

        var res = assertThrows(CategoriaConflictException.class, () -> service.updateCategoria(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"), CategoriaCreateMapper.toDto(category1)));
        assertEquals("Ya existe una categoria con el nombre: categoria 1", res.getMessage());
    }

    @Test
    void deleteCategoriaWithBooks() {
        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))).thenReturn(Optional.of(category1));
        when(bookRepository.findByCategory_Name(any())).thenReturn(new ArrayList<>());
        doThrow(new CategoriaConflictException("No se puede eliminar la categoría porque tiene libros asociados")).when(repository).deleteById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));

        var res = assertThrows(CategoriaConflictException.class, () -> service.deleteById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")));
        assertEquals("No se puede eliminar la categoría porque tiene libros asociados", res.getMessage());
    }


    @Test
    void deleteCategoriaNotFound() {
        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-999966fa9734"))).thenReturn(Optional.empty());

        var res = assertThrows(CategoriaNotFoundException.class, () -> service.deleteById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-999966fa9734")));
        assertEquals("Categoria con id 3930e05a-7ebf-4aa1-8aa8-999966fa9734 no encontrada", res.getMessage());
    }

    @Test
    void deleteCategoria() {
        List<GetBookDTO> expectedbooks = List.of();

        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))).thenReturn(Optional.of(category1));
        when(bookRepository.findByCategory_Name(any())).thenReturn(new ArrayList<>());

        service.deleteById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));
    }


}
