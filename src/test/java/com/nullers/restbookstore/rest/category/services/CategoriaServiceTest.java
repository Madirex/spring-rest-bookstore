package com.nullers.restbookstore.rest.category.services;


import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.services.BookServiceImpl;
import com.nullers.restbookstore.rest.category.exceptions.CategoriaConflictException;
import com.nullers.restbookstore.rest.category.exceptions.CategoriaNotFoundException;
import com.nullers.restbookstore.rest.category.mappers.CategoriaCreateMapper;
import com.nullers.restbookstore.rest.category.models.Categoria;
import com.nullers.restbookstore.rest.category.repository.CategoriasRepositoryJpa;
import com.nullers.restbookstore.rest.category.services.CategoriaServiceJpaImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @Mock
    private CategoriasRepositoryJpa repository;

    @Mock
    private BookServiceImpl bookService;

    @InjectMocks
    private CategoriaServiceJpaImpl service;

    Categoria categoria1 = Categoria.builder()
            .id(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))
            .nombre("categoria 1")
            .activa(true)
            .build();

    Categoria categoria2 = Categoria.builder()
            .id(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9735"))
            .nombre("categoria 2")
            .activa(true)
            .build();

    @Test
    void getAll_ShoulReturnAllBooksWithoutParamsPageable(){
        List<Categoria> expectedCategorias = List.of(categoria1, categoria2);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        Page<Categoria> expectedPage = new PageImpl(expectedCategorias);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Categoria> result = service.getAll(Optional.empty(), Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(expectedPage.getTotalPages(), result.getTotalPages()),
                () -> assertEquals(expectedPage.getTotalElements(), result.getTotalElements()),
                () -> assertEquals(expectedPage.getNumber(), result.getNumber()),
                () -> assertEquals(expectedPage.getNumberOfElements(), result.getNumberOfElements()),
                () -> assertEquals(expectedPage.getSize(), result.getSize()),
                () -> assertEquals(expectedPage.getContent().get(0).getNombre(), result.getContent().get(0).getNombre()),
                () -> assertEquals(expectedPage.getContent().get(0).isActiva(), result.getContent().get(0).isActiva()),
                () -> assertEquals(expectedPage.getContent().get(1).getNombre(), result.getContent().get(1).getNombre()),
                () -> assertEquals(expectedPage.getContent().get(1).isActiva(), result.getContent().get(1).isActiva())
        );

    }

    @Test
    void getAll_ShoulReturnAllBooksNameParamPageable(){
        List<Categoria> expectedCategorias = List.of(categoria1);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        Page<Categoria> expectedPage = new PageImpl(expectedCategorias);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Categoria> result = service.getAll(Optional.of("categoria 1"), Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(expectedPage.getTotalPages(), result.getTotalPages()),
                () -> assertEquals(expectedPage.getTotalElements(), result.getTotalElements()),
                () -> assertEquals(expectedPage.getNumber(), result.getNumber()),
                () -> assertEquals(expectedPage.getNumberOfElements(), result.getNumberOfElements()),
                () -> assertEquals(expectedPage.getSize(), result.getSize()),
                () -> assertEquals(categoria1.getNombre(), result.getContent().get(0).getNombre()),
                () -> assertEquals(categoria1.isActiva(), result.getContent().get(0).isActiva())
        );
    }

    @Test
    void getAll_ShoulReturnAllBooksActivaParamPageable(){
        List<Categoria> expectedCategorias = List.of(categoria1);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        Page<Categoria> expectedPage = new PageImpl(expectedCategorias);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Categoria> result = service.getAll(Optional.empty(), Optional.of(true), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(expectedPage.getTotalPages(), result.getTotalPages()),
                () -> assertEquals(expectedPage.getTotalElements(), result.getTotalElements()),
                () -> assertEquals(expectedPage.getNumber(), result.getNumber()),
                () -> assertEquals(expectedPage.getNumberOfElements(), result.getNumberOfElements()),
                () -> assertEquals(expectedPage.getSize(), result.getSize()),
                () -> assertEquals(categoria1.getNombre(), result.getContent().get(0).getNombre()),
                () -> assertEquals(categoria1.isActiva(), result.getContent().get(0).isActiva())
        );
    }

    @Test
    void getAll_ShoulReturnAllBooksActivaFalseParamPageable(){
        List<Categoria> expectedCategorias = List.of();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        Page<Categoria> expectedPage = new PageImpl(expectedCategorias);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Categoria> result = service.getAll(Optional.empty(), Optional.of(false), pageable);

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
    void getCategoriaById(){
        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))).thenReturn(Optional.of(categoria1));

        var categoria = service.getCategoriaById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));

        assertAll(
                () -> assertNotNull(categoria),
                () -> assertEquals(categoria1.getNombre(), categoria.getNombre()),
                () -> assertEquals(categoria1.isActiva(), categoria.isActiva())
        );
    }

    @Test
    void getCategoriaByIdNotFound(){
        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466f99999"))).thenReturn(Optional.empty());

        var res = assertThrows(CategoriaNotFoundException.class, () -> service.getCategoriaById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466f99999")));
        assertEquals("Categoria con id 3930e05a-7ebf-4aa1-8aa8-5d7466f99999 no encontrada", res.getMessage());
    }

    @Test
    void getCategoriaByNombre(){
        when(repository.findByNombre("categoria 1")).thenReturn(Optional.of(categoria1));

        var categoria = service.getCategoriaByNombre("categoria 1");

        assertAll(
                () -> assertNotNull(categoria),
                () -> assertEquals(categoria1.getNombre(), categoria.getNombre()),
                () -> assertEquals(categoria1.isActiva(), categoria.isActiva())
        );
    }

    @Test
    void getCategoriaByNombreNotFound(){
        when(repository.findByNombre("categoria 99")).thenReturn(Optional.empty());

        var res = assertThrows(CategoriaNotFoundException.class, () -> service.getCategoriaByNombre("categoria 99"));
        assertEquals("Categoria con nombre categoria 99 no encontrada", res.getMessage());
    }

    @Test
    void createCategoria(){
        when(repository.save(any(Categoria.class))).thenReturn(categoria1);
        when(repository.findByNombre("categoria 1")).thenReturn(Optional.empty());

        var categoria = service.createCategoria(CategoriaCreateMapper.toDto(categoria1));

        assertAll(
                () -> assertNotNull(categoria),
                () -> assertEquals(categoria1.getNombre(), categoria.getNombre()),
                () -> assertEquals(categoria1.isActiva(), categoria.isActiva())
        );
    }

    @Test
    void createCategoriaConflict(){
        when(repository.findByNombre("categoria 1")).thenReturn(Optional.of(categoria1));


        var res = assertThrows(CategoriaConflictException.class, () -> service.createCategoria(CategoriaCreateMapper.toDto(categoria1)));
        assertEquals("Ya existe una categoria con el nombre: categoria 1", res.getMessage());
    }

    @Test
    void updateCategoria(){
        when(repository.save(any(Categoria.class))).thenReturn(categoria1);
        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))).thenReturn(Optional.of(categoria1));
        when(repository.findByNombre("categoria 1")).thenReturn(Optional.empty());

        var categoria = service.updateCategoria(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"), CategoriaCreateMapper.toDto(categoria1));

        assertAll(
                () -> assertNotNull(categoria),
                () -> assertEquals(categoria1.getNombre(), categoria.getNombre()),
                () -> assertEquals(categoria1.isActiva(), categoria.isActiva())
        );
    }

    @Test
    void updateCategoriaNotFound(){
        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-999966fa9734"))).thenReturn(Optional.empty());

        var res = assertThrows(CategoriaNotFoundException.class, () -> service.updateCategoria(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-999966fa9734"), CategoriaCreateMapper.toDto(categoria1)));
        assertEquals("Categoria con id 3930e05a-7ebf-4aa1-8aa8-999966fa9734 no encontrada", res.getMessage());
    }

    @Test
    void updateCategoriaConflict(){
        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))).thenReturn(Optional.of(categoria1));
        when(repository.findByNombre("categoria 1")).thenReturn(Optional.of(categoria2));

        var res = assertThrows(CategoriaConflictException.class, () -> service.updateCategoria(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"), CategoriaCreateMapper.toDto(categoria1)));
        assertEquals("Ya existe una categoria con el nombre: categoria 1", res.getMessage());
    }

    @Test
    void deleteCategoriaWithBooks(){
        List<Categoria> expectedCategorias = List.of(categoria1);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        Page<Categoria> expectedPage = new PageImpl(expectedCategorias);
        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))).thenReturn(Optional.of(categoria1));
        when(bookService.getAllBook(any(Optional.class),any(Optional.class),any(Optional.class), any(PageRequest.class))).thenReturn(expectedPage);

        var res = assertThrows(CategoriaConflictException.class, () -> service.deleteById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")));
        assertEquals("No se puede eliminar la categoria porque tiene libros asociados", res.getMessage());
    }


    @Test
    void deleteCategoriaNotFound(){
        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-999966fa9734"))).thenReturn(Optional.empty());

        var res = assertThrows(CategoriaNotFoundException.class, () -> service.deleteById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-999966fa9734")));
        assertEquals("Categoria con id 3930e05a-7ebf-4aa1-8aa8-999966fa9734 no encontrada", res.getMessage());
    }

    @Test
    void deleteCategoria(){
        List<GetBookDTO> expectedbooks = List.of();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        Page<GetBookDTO> expectedPage = new PageImpl(expectedbooks);
        when(repository.findById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))).thenReturn(Optional.of(categoria1));
        when(bookService.getAllBook(any(Optional.class),any(Optional.class),any(Optional.class), any(PageRequest.class))).thenReturn(expectedPage);

        service.deleteById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));
    }


}
