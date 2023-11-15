package com.nullers.restbookstore.shop.mappers;

import com.nullers.restbookstore.shop.dto.CreateShopDto;
import com.nullers.restbookstore.shop.dto.GetShopDto;
import com.nullers.restbookstore.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.shop.model.Shop;

import java.util.List;
/**
 * Clase ShopMapper
 */




public interface ShopMapper {
    Shop toShop(CreateShopDto dto);

    Shop toShop(Shop shop, UpdateShopDto dto);

    GetShopDto toGetShopDto(Shop shop);

    List<GetShopDto> toShopList(List<Shop> shops);
}

