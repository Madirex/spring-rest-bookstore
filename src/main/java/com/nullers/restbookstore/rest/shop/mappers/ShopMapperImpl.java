package com.nullers.restbookstore.rest.shop.mappers;


import com.nullers.restbookstore.rest.book.model.Book;
import com.nullers.restbookstore.rest.client.model.Client;
import com.nullers.restbookstore.rest.shop.dto.CreateShopDto;
import com.nullers.restbookstore.rest.shop.dto.GetShopDto;
import com.nullers.restbookstore.rest.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.rest.shop.model.Shop;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Clase ShopMapperImpl
 *
 * @author alexdor00
 */

@Component
public class ShopMapperImpl implements ShopMapper {

    /**
     * Método para convertir un CreateShopDto a un objeto Shop
     *
     * @param dto CreateShopDto
     * @return Shop
     */
    public Shop toShop(CreateShopDto dto) {
        return Shop.builder()
                .id(UUID.randomUUID())
                .name(dto.getName())
                .location(dto.getLocation())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Método para convertir un UpdateShopDto a un objeto Shop
     *
     * @param shop Shop
     * @param dto  UpdateShopDto
     * @return Shop
     */
    public Shop toShop(Shop shop, UpdateShopDto dto) {
        shop.setName(dto.getName());
        shop.setLocation(dto.getLocation());
        shop.setUpdatedAt(LocalDateTime.now());
        return shop;
    }

    /**
     * Método para convertir un objeto Shop a un GetShopDto
     *
     * @param shop Shop
     * @return GetShopDto
     */
    public GetShopDto toGetShopDto(Shop shop) {
        return GetShopDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .location(shop.getLocation())
                .createdAt(shop.getCreatedAt())
                .updatedAt(shop.getUpdatedAt())
                .booksId(shop.getBooks().stream().map(Book::getId).collect(Collectors.toSet()))
                .clientsId(shop.getClients().stream().map(Client::getId).collect(Collectors.toSet()))
                .build();
    }

    /**
     * Método para convertir una lista de objetos Shop a una lista de GetShopDto
     *
     * @param shops Lista de objetos Shop
     * @return Lista de GetShopDto
     */
    public List<GetShopDto> toShopList(List<Shop> shops) {
        return shops.stream()
                .map(this::toGetShopDto)
                .toList();
    }
}
