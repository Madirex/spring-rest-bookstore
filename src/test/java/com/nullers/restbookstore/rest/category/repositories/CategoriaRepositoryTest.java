package com.nullers.restbookstore.rest.category.repositories;

import com.nullers.restbookstore.rest.category.exceptions.CategoriaNotFoundException;
import com.nullers.restbookstore.rest.category.models.Categoria;
import com.nullers.restbookstore.rest.category.repository.CategoriasRepositoryJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoriaRepositoryTest {

    @Autowired
    private CategoriasRepositoryJpa categoriaRepositoryJpa;

    @Autowired
    private TestEntityManager entityManager;

    Categoria categoria1 = Categoria.builder()
            .id(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))
            .nombre("categoria 1")
            .activa(true)
            .build();

    @Test
    void findAll(){
        entityManager.merge(categoria1);
        entityManager.flush();

        var categorias = categoriaRepositoryJpa.findAll();

        assertAll(
                () -> assertNotNull(categorias),
                () -> assertTrue(!categorias.isEmpty())
        );
    }

    @Test
    void getCategoriaById(){
        var res = entityManager.merge(categoria1);
        entityManager.flush();

        var categoria = categoriaRepositoryJpa.findById(res.getId());

        assertAll(
                () -> assertNotNull(categoria),
                () -> assertEquals(categoria1.getNombre(), categoria.get().getNombre()),
                () -> assertEquals(categoria1.isActiva(), categoria.get().isActiva())
        );
    }

    @Test
    void getCategoriaByNombre(){
        var res = entityManager.merge(categoria1);
        entityManager.flush();

        var categoria = categoriaRepositoryJpa.findByNombre(res.getNombre());

        assertAll(
                () -> assertNotNull(categoria),
                () -> assertEquals(categoria1.getNombre(), categoria.get().getNombre()),
                () -> assertEquals(categoria1.isActiva(), categoria.get().isActiva())
        );
    }

    @Test
    void createCategoria(){
        var categoria = categoriaRepositoryJpa.save(categoria1);

        assertAll(
                () -> assertNotNull(categoria),
                () -> assertEquals(categoria1.getNombre(), categoria.getNombre()),
                () -> assertEquals(categoria1.isActiva(), categoria.isActiva())
        );
    }

    @Test
    void updateCategoria(){
        var res = entityManager.merge(categoria1);
        entityManager.flush();

        var categoria = categoriaRepositoryJpa.save(res);

        assertAll(
                () -> assertNotNull(categoria),
                () -> assertEquals(categoria1.getNombre(), categoria.getNombre()),
                () -> assertEquals(categoria1.isActiva(), categoria.isActiva())
        );
    }


    @Test
    void deleteCategoria(){
        var res = entityManager.merge(categoria1);
        entityManager.flush();

        categoriaRepositoryJpa.deleteById(res.getId());

        assertThrows(CategoriaNotFoundException.class, () -> categoriaRepositoryJpa.findById(res.getId()).orElseThrow(() -> new CategoriaNotFoundException(res.getId())));
    }

}
