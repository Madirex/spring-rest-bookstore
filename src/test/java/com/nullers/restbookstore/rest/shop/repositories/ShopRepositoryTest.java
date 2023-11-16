package com.nullers.restbookstore.rest.shop.repositories;



import com.nullers.restbookstore.shop.model.Shop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ShopRepositoryTest {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private TestEntityManager entityManager;

    Shop shopTest;

    @BeforeEach
    void setUp() {
        shopRepository.deleteAll();
        shopTest = Shop.builder()
                .name("Test Shop")
                .location("Test Location")
                .build();
    }

    @Test
    void findAllTest() {
        entityManager.persistAndFlush(shopTest);

        var shops = shopRepository.findAll();

        assertAll(
                () -> assertNotNull(shops),
                () -> assertFalse(shops.isEmpty()),
                () -> assertEquals(1, shops.size()),
                () -> assertEquals(shopTest.getName(), shops.get(0).getName()),
                () -> assertEquals(shopTest.getLocation(), shops.get(0).getLocation())
        );
    }

    @Test
    void findByIdTest() {
        var persistedShop = entityManager.persistFlushFind(shopTest);

        var foundShop = shopRepository.findById(persistedShop.getId());

        assertAll(
                () -> assertTrue(foundShop.isPresent()),
                () -> assertEquals(shopTest.getName(), foundShop.get().getName()),
                () -> assertEquals(shopTest.getLocation(), foundShop.get().getLocation())
        );
    }

    @Test
    void findByIdNotFoundTest() {
        var randomUUID = UUID.randomUUID();

        var foundShop = shopRepository.findById(randomUUID);

        assertTrue(foundShop.isEmpty());
    }

    @Test
    void saveTest() {
        var savedShop = shopRepository.save(shopTest);

        assertNotNull(savedShop.getId());
        assertEquals(shopTest.getName(), savedShop.getName());
        assertEquals(shopTest.getLocation(), savedShop.getLocation());
    }

    @Test
    void updateTest() {
        var persistedShop = entityManager.persistFlushFind(shopTest);

        persistedShop.setName("Updated Name");
        persistedShop.setLocation("Updated Location");
        var updatedShop = shopRepository.save(persistedShop);

        assertEquals("Updated Name", updatedShop.getName());
        assertEquals("Updated Location", updatedShop.getLocation());
    }

    @Test
    void deleteTest() {
        var persistedShop = entityManager.persistFlushFind(shopTest);

        shopRepository.delete(persistedShop);

        var foundShop = shopRepository.findById(persistedShop.getId());
        assertTrue(foundShop.isEmpty());
    }
}

