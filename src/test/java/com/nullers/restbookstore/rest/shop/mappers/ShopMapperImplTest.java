package com.nullers.restbookstore.rest.shop.mappers;

import com.nullers.restbookstore.shop.dto.*;
import com.nullers.restbookstore.shop.mappers.ShopMapperImpl;
import com.nullers.restbookstore.shop.model.Shop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase ShopMapperImplTest
 *
 */
class ShopMapperImplTest {
    private ShopMapperImpl shopMapper;

    /**
     * Método setUp para inicializar los objetos
     */
    @BeforeEach
    void setUp() {
        shopMapper = new ShopMapperImpl();
    }

    /**
     * Test para comprobar que el mapeo de CreateShopDto a Shop es correcto
     */
    @Test
    void testCreateShopDtoToShop() {
        CreateShopDto createShopDto = CreateShopDto.builder()
                .name("Tienda ")
                .location("Ubicación ")
                .build();
        Shop mapped = shopMapper.toShop(createShopDto);
        assertAll("Shop properties",
                () -> assertEquals(createShopDto.getName(), mapped.getName(), "El nombre debe coincidir"),
                () -> assertEquals(createShopDto.getLocation(), mapped.getLocation(), "La ubicación debe coincidir"),
                () -> assertNotNull(mapped.getCreatedAt(), "La fecha de creación no debe ser nula"),
                () -> assertNotNull(mapped.getUpdatedAt(), "La fecha de actualización no debe ser nula"),
                () -> assertTrue(mapped.getActive(), "La tienda debe estar activa por defecto")
        );
    }

    /**
     * Test para comprobar que el mapeo de UpdateShopDto a Shop es correcto
     */
    @Test
    void testUpdateShopDtoToShop() {
        Shop originalShop = Shop.builder()
                .id(UUID.randomUUID())
                .name("Nombre Original")
                .location("Ubicación Original")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .active(true)
                .build();
        UpdateShopDto updateShopDto = UpdateShopDto.builder()
                .name("Nombre Actualizado")
                .location("Ubicación Actualizada")
                .build();
        Shop mapped = shopMapper.toShop(originalShop, updateShopDto);
        assertAll("Shop properties",
                () -> assertNotNull(mapped.getId(), "El ID no debe ser nulo"),
                () -> assertEquals(updateShopDto.getName(), mapped.getName(), "El nombre debe coincidir"),
                () -> assertEquals(updateShopDto.getLocation(), mapped.getLocation(), "La ubicación debe coincidir"),
                () -> assertNotNull(mapped.getCreatedAt(), "La fecha de creación no debe ser nula"),
                () -> assertNotNull(mapped.getUpdatedAt(), "La fecha de actualización no debe ser nula")
        );
    }

    /**
     * Test para comprobar que el mapeo de Shop a GetShopDto es correcto
     */
    @Test
    void toGetShopDto() {
        Shop shop = Shop.builder()
                .id(UUID.randomUUID())
                .name("Tienda Test")
                .location("Ubicación Test")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .active(true)
                .build();
        GetShopDto mapped = shopMapper.toGetShopDto(shop);
        assertAll("Shop properties",
                () -> assertEquals(shop.getId(), mapped.getId(), "El ID debe coincidir"),
                () -> assertEquals(shop.getName(), mapped.getName(), "El nombre debe coincidir"),
                () -> assertEquals(shop.getLocation(), mapped.getLocation(), "La ubicación debe coincidir"),
                () -> assertEquals(shop.getCreatedAt(), mapped.getCreatedAt(), "La fecha de creación debe coincidir"),
                () -> assertEquals(shop.getUpdatedAt(), mapped.getUpdatedAt(), "La fecha de actualización debe coincidir")
        );
    }

    /**
     * Test para comprobar mapeo a GetShopDtoList
     */
    @Test
    void toGetShopDtoList() {
        List<Shop> list = new ArrayList<>();
        list.add(Shop.builder()
                .id(UUID.randomUUID())
                .name("Tienda Test")
                .location("Ubicación Test")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .active(true)
                .build());
        List<GetShopDto> mapped = shopMapper.toShopList(list);
        assertAll("Shop properties",
                () -> assertNotNull(mapped.get(0).getId(), "El ID no debe ser nulo"),
                () -> assertEquals(1, mapped.size(), "La lista debe contener 1 elemento"),
                () -> assertEquals(list.get(0).getName(), mapped.get(0).getName(), "El nombre debe coincidir"),
                () -> assertEquals(list.get(0).getLocation(), mapped.get(0).getLocation(), "La ubicación debe coincidir"),
                () -> assertEquals(list.get(0).getCreatedAt(), mapped.get(0).getCreatedAt(), "La fecha de creación debe coincidir"),
                () -> assertEquals(list.get(0).getUpdatedAt(), mapped.get(0).getUpdatedAt(), "La fecha de actualización debe coincidir")
        );
    }
}
