package com.nullers.restbookstore.rest.category.repositories;

import com.nullers.restbookstore.rest.category.exceptions.CategoriaNotFoundException;
import com.nullers.restbookstore.rest.category.model.Category;
import com.nullers.restbookstore.rest.category.repository.CategoriasRepositoryJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoriasRepositoryJpa categoriaRepositoryJpa;

    @Autowired
    private TestEntityManager entityManager;

    Category category1 = Category.builder()
            .id(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))
            .name("categoria 1")
            .isActive(true)
            .build();

    @Test
    void findAll(){
        entityManager.merge(category1);
        entityManager.flush();

        var categorias = categoriaRepositoryJpa.findAll();

        assertAll(
                () -> assertNotNull(categorias),
                () -> assertTrue(!categorias.isEmpty())
        );
    }

    @Test
    void getCategoriaById(){
        var res = entityManager.merge(category1);
        entityManager.flush();

        var categoria = categoriaRepositoryJpa.findById(res.getId());

        assertAll(
                () -> assertNotNull(categoria),
                () -> assertEquals(category1.getName(), categoria.get().getName()),
                () -> assertEquals(category1.getIsActive(), categoria.get().getIsActive())
        );
    }

    @Test
    void getCategoriaByNombre(){
        var res = entityManager.merge(category1);
        entityManager.flush();

        var categoria = categoriaRepositoryJpa.findByNombre(res.getName());

        assertAll(
                () -> assertNotNull(categoria),
                () -> assertEquals(category1.getName(), categoria.get().getName()),
                () -> assertEquals(category1.getIsActive(), categoria.get().getIsActive())
        );
    }

    @Test
    void createCategoria(){
        var categoria = categoriaRepositoryJpa.save(category1);

        assertAll(
                () -> assertNotNull(categoria),
                () -> assertEquals(category1.getName(), categoria.getName()),
                () -> assertEquals(category1.getIsActive(), categoria.getIsActive())
        );
    }

    @Test
    void updateCategoria(){
        var res = entityManager.merge(category1);
        entityManager.flush();

        var categoria = categoriaRepositoryJpa.save(res);

        assertAll(
                () -> assertNotNull(categoria),
                () -> assertEquals(category1.getName(), categoria.getName()),
                () -> assertEquals(category1.getIsActive(), categoria.getIsActive())
        );
    }


    @Test
    void deleteCategoria(){
        var res = entityManager.merge(category1);
        entityManager.flush();

        categoriaRepositoryJpa.deleteById(res.getId());

        assertThrows(CategoriaNotFoundException.class, () -> categoriaRepositoryJpa.findById(res.getId()).orElseThrow(() -> new CategoriaNotFoundException(res.getId())));
    }

}
