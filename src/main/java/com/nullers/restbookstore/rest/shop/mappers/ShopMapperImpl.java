package com.nullers.restbookstore.rest.shop.mappers;


import com.nullers.restbookstore.rest.book.model.Book;
import com.nullers.restbookstore.rest.client.model.Client;
import com.nullers.restbookstore.rest.shop.dto.CreateShopDto;
import com.nullers.restbookstore.rest.shop.dto.GetShopDto;
import com.nullers.restbookstore.rest.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.rest.shop.model.Shop;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Clase ShopMapperImpl
 *
 *  @author alexdor00
 */

@Component
public class ShopMapperImpl implements ShopMapper {

    /**
     * Crea una nueva entidad Shop a partir de un CreateShopDto.
     *
     * @param dto el DTO que contiene la información para crear una nueva tienda.
     * @return una nueva entidad Shop con información obtenida del DTO.
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
     * Actualiza una entidad Shop existente con la información proporcionada en UpdateShopDto.
     *
     * @param shop la entidad Shop existente que se va a actualizar.
     * @param dto el objeto DTO que contiene la información para actualizar la tienda.
     * @return un objeto Shop actualizado con la información del DTO.
     */

    public Shop toShop(Shop shop, UpdateShopDto dto) {
        shop.setName(dto.getName());
        shop.setLocation(dto.getLocation());
        shop.setUpdatedAt(LocalDateTime.now());
        return shop;
    }

    /**
     * Convierte una entidad Shop a un GetShopDto.
     *
     * @param shop el objeto Shop que se va a convertir.
     * @return un objeto GetShopDto que representa la información de la tienda.
     */
    public GetShopDto toGetShopDto(Shop shop) {
        return GetShopDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .location(shop.getLocation())
                .createdAt(shop.getCreatedAt())
                .updatedAt(shop.getUpdatedAt())
                .books_id(shop.getBooks().stream().map(Book::getId).collect(Collectors.toSet()))
                .clients_id(shop.getClients().stream().map(Client::getId).collect(Collectors.toSet()))
                .build();
    }



    /**
     * Convierte una lista de entidades Shop a una lista de GetShopDto.
     *
     * @param shops una lista de objetos Shop que se van a convertir.
     * @return una lista de objetos GetShopDto correspondientes a las tiendas.
     */
    public List<GetShopDto> toShopList(List<Shop> shops) {
        return shops.stream()
                .map(this::toGetShopDto)
                .collect(Collectors.toList());
    }
}
