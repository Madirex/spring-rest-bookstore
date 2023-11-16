package com.nullers.restbookstore.shop.controllers;

import com.nullers.restbookstore.shop.dto.CreateShopDto;
import com.nullers.restbookstore.shop.dto.GetShopDto;
import com.nullers.restbookstore.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.shop.exceptions.ShopNotFoundException;
import com.nullers.restbookstore.shop.exceptions.ShopNotValidUUIDException;
import com.nullers.restbookstore.shop.services.ShopServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Clase ShopRestContoller
 *
 * @author alexdor00
 */


@RestController
@RequestMapping("/api/shops")
public class ShopRestControllerImpl implements ShopRestController {

    private final ShopServiceImpl shopService;

    @Autowired
    public ShopRestControllerImpl(ShopServiceImpl shopService) {
        this.shopService = shopService;
    }

    @GetMapping
    @Override
    public ResponseEntity<Page<GetShopDto>> getAllShops(Pageable pageable) {
        return ResponseEntity.ok(shopService.getAllShops(pageable));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<GetShopDto> getShopById(@PathVariable String id) throws ShopNotValidUUIDException, ShopNotFoundException {
        try {
            return ResponseEntity.ok(shopService.getShopById(id));
        } catch (IllegalArgumentException e) {
            throw new ShopNotValidUUIDException("UUID no válido: " + id);
        }
    }

    @PostMapping
    @Override
    public ResponseEntity<GetShopDto> createShop(@RequestBody CreateShopDto shopDto) {
        GetShopDto newShop = shopService.createShop(shopDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newShop);
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<GetShopDto> updateShop(@PathVariable String id, @RequestBody UpdateShopDto shopDto) throws ShopNotValidUUIDException, ShopNotFoundException {
        try {
            return ResponseEntity.ok(shopService.updateShop(id, shopDto));
        } catch (IllegalArgumentException e) {
            throw new ShopNotValidUUIDException("UUID no válido: " + id);
        }
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<String> deleteShop(@PathVariable String id) throws ShopNotValidUUIDException, ShopNotFoundException {
        try {
            shopService.deleteShop(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ShopNotValidUUIDException("UUID no válido: " + id);
        }
    }
}
