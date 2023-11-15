package com.nullers.restbookstore.shop.controllers;


import com.nullers.restbookstore.shop.dto.CreateShopDto;
import com.nullers.restbookstore.shop.dto.GetShopDto;
import com.nullers.restbookstore.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.shop.exceptions.ShopNotFoundException;
import com.nullers.restbookstore.shop.exceptions.ShopNotValidUUIDException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Interface ShopRestController
 */
public interface ShopRestController {
    ResponseEntity<List<GetShopDto>> getAllShops();

    ResponseEntity<GetShopDto> getShopById(@Valid @PathVariable String id)
            throws ShopNotValidUUIDException, ShopNotFoundException;

    ResponseEntity<GetShopDto> createShop(@Valid @RequestBody CreateShopDto shopDto);

    ResponseEntity<GetShopDto> updateShop(@Valid @PathVariable String id, @Valid @RequestBody UpdateShopDto shopDto)
            throws ShopNotValidUUIDException, ShopNotFoundException;

    ResponseEntity<String> deleteShop(@Valid @PathVariable String id) throws ShopNotValidUUIDException, ShopNotFoundException;
}
