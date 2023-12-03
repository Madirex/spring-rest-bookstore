package com.nullers.restbookstore.rest.shop.mappers;

import com.nullers.restbookstore.rest.shop.dto.CreateShopDto;
import com.nullers.restbookstore.rest.shop.dto.GetShopDto;
import com.nullers.restbookstore.rest.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.rest.shop.model.Shop;

import java.util.List;
/**
 * Clase ShopMapper
 *
 *  @author alexdor00
 */



public interface ShopMapper {

    /**
     * Convierte un CreateShopDto a una entidad Shop.
     *
     * @param dto el objeto DTO que contiene la información para crear una tienda.
     * @return un objeto Shop creado a partir de los datos proporcionados en el DTO.
     */
    Shop toShop(CreateShopDto dto);

    /**
     * Actualiza una entidad Shop existente con la información proporcionada en UpdateShopDto.
     *
     * @param shop la entidad Shop existente que se va a actualizar.
     * @param dto el objeto DTO que contiene la información para actualizar la tienda.
     * @return un objeto Shop actualizado con la información del DTO.
     */
    Shop toShop(Shop shop, UpdateShopDto dto);

    /**
     * Convierte una entidad Shop a un GetShopDto.
     *
     * @param shop el objeto Shop que se va a convertir.
     * @return un objeto GetShopDto que representa la información de la tienda.
     */
    GetShopDto toGetShopDto(Shop shop);

    /**
     * Convierte una lista de entidades Shop a una lista de GetShopDto.
     *
     * @param shops una lista de objetos Shop que se van a convertir.
     * @return una lista de objetos GetShopDto correspondientes a las tiendas.
     */
    List<GetShopDto> toShopList(List<Shop> shops);
}
