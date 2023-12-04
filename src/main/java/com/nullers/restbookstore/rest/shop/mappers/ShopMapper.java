package com.nullers.restbookstore.rest.shop.mappers;

import com.nullers.restbookstore.rest.shop.dto.CreateShopDto;
import com.nullers.restbookstore.rest.shop.dto.GetShopDto;
import com.nullers.restbookstore.rest.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.rest.shop.model.Shop;

import java.util.List;

/**
 * Clase ShopMapper
 *
 * @author alexdor00
 */
public interface ShopMapper {
    /**
     * Método para convertir un CreateShopDto a un objeto Shop
     *
     * @param dto CreateShopDto
     * @return Shop
     */
    Shop toShop(CreateShopDto dto);

    /**
     * Método para convertir un UpdateShopDto a un objeto Shop
     *
     * @param shop Shop
     * @param dto  UpdateShopDto
     * @return Shop
     */
    Shop toShop(Shop shop, UpdateShopDto dto);

    /**
     * Método para convertir un objeto Shop a un GetShopDto
     *
     * @param shop Shop
     * @return GetShopDto
     */
    GetShopDto toGetShopDto(Shop shop);

    /**
     * Método para convertir una lista de objetos Shop a una lista de GetShopDto
     *
     * @param shops Lista de objetos Shop
     * @return Lista de GetShopDto
     */
    List<GetShopDto> toShopList(List<Shop> shops);
}

