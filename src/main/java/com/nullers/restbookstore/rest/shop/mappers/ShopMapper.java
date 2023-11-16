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
    Shop toShop(CreateShopDto dto);

    Shop toShop(Shop shop, UpdateShopDto dto);

    GetShopDto toGetShopDto(Shop shop);

    List<GetShopDto> toShopList(List<Shop> shops);
}

