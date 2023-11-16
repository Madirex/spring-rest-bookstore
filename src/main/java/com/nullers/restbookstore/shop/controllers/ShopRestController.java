package com.nullers.restbookstore.shop.controllers;

import com.nullers.restbookstore.shop.dto.CreateShopDto;
import com.nullers.restbookstore.shop.dto.GetShopDto;
import com.nullers.restbookstore.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.shop.exceptions.ShopNotFoundException;
import com.nullers.restbookstore.shop.exceptions.ShopNotValidUUIDException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
/**
 * Clase ShopRestContoller
 *
 * @author alexdor00
 */
public interface ShopRestController {

    ResponseEntity<Page<GetShopDto>> getAllShops(Pageable pageable);

    ResponseEntity<GetShopDto> getShopById(String id) throws ShopNotValidUUIDException, ShopNotFoundException;

    ResponseEntity<GetShopDto> createShop(CreateShopDto shopDto);

    ResponseEntity<GetShopDto> updateShop(String id, UpdateShopDto shopDto) throws ShopNotValidUUIDException, ShopNotFoundException;

    ResponseEntity<String> deleteShop(String id) throws ShopNotValidUUIDException, ShopNotFoundException;
}
