package com.nullers.restbookstore.rest.category.repositories;

import com.nullers.restbookstore.rest.category.exceptions.CategoryNotFoundException;
import com.nullers.restbookstore.rest.category.model.Category;
import com.nullers.restbookstore.rest.category.repository.CategoryRepositoryJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepositoryJpa categoryRepositoryJpa;

    @Autowired
    private TestEntityManager entityManager;

    Category category1 = Category.builder()
            .id(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))
            .name("category 1")
            .isActive(true)
            .build();

    @Test
    void findAll(){
        entityManager.merge(category1);
        entityManager.flush();

        var categories = categoryRepositoryJpa.findAll();

        assertAll(
                () -> assertNotNull(categories),
                () -> assertTrue(!categories.isEmpty())
        );
    }

    @Test
    void getCategoryById(){
        var res = entityManager.merge(category1);
        entityManager.flush();

        var category = categoryRepositoryJpa.findById(res.getId());

        assertAll(
                () -> assertNotNull(category),
                () -> assertEquals(category1.getName(), category.get().getName()),
                () -> assertEquals(category1.getIsActive(), category.get().getIsActive())
        );
    }

    @Test
    void getCategoryByName(){
        var res = entityManager.merge(category1);
        entityManager.flush();

        var category = categoryRepositoryJpa.findByNameEqualsIgnoreCase(res.getName());

        assertAll(
                () -> assertNotNull(category),
                () -> assertEquals(category1.getName(), category.get().getName()),
                () -> assertEquals(category1.getIsActive(), category.get().getIsActive())
        );
    }

    @Test
    void createCategory(){
        var category = categoryRepositoryJpa.save(category1);

        assertAll(
                () -> assertNotNull(category),
                () -> assertEquals(category1.getName(), category.getName()),
                () -> assertEquals(category1.getIsActive(), category.getIsActive())
        );
    }

    @Test
    void updateCategory(){
        var res = entityManager.merge(category1);
        entityManager.flush();

        var category = categoryRepositoryJpa.save(res);

        assertAll(
                () -> assertNotNull(category),
                () -> assertEquals(category1.getName(), category.getName()),
                () -> assertEquals(category1.getIsActive(), category.getIsActive())
        );
    }


    @Test
    void deleteCategory(){
        var res = entityManager.merge(category1);
        entityManager.flush();

        categoryRepositoryJpa.deleteById(res.getId());

        assertThrows(CategoryNotFoundException.class, () -> categoryRepositoryJpa.findById(res.getId()).orElseThrow(() -> new CategoryNotFoundException(res.getId())));
    }

}
