package com.nullers.restbookstore.shop.services;

import com.nullers.restbookstore.shop.dto.CreateShopDto;
import com.nullers.restbookstore.shop.dto.GetShopDto;
import com.nullers.restbookstore.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.shop.exceptions.ShopNotFoundException;
import com.nullers.restbookstore.shop.exceptions.ShopNotValidUUIDException;

import java.util.List;

public interface ShopService {
    List<GetShopDto> getAllShops();

    GetShopDto getShopById(String id) throws ShopNotValidUUIDException, ShopNotFoundException;

    GetShopDto createShop(CreateShopDto shopDto);

    GetShopDto updateShop(String id, UpdateShopDto shopDto) throws ShopNotValidUUIDException, ShopNotFoundException;

    void deleteShop(String id) throws ShopNotFoundException, ShopNotValidUUIDException;
}
