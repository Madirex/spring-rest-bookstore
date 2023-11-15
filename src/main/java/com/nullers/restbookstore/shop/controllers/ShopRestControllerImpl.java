package com.nullers.restbookstore.shop.controllers;

import com.nullers.restbookstore.shop.dto.CreateShopDto;
import com.nullers.restbookstore.shop.dto.GetShopDto;
import com.nullers.restbookstore.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.shop.exceptions.ShopNotFoundException;
import com.nullers.restbookstore.shop.exceptions.ShopNotValidUUIDException;
import com.nullers.restbookstore.shop.services.ShopServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clase ShopRestControllerImpl
 */
@RestController
@RequestMapping("/api/shops")
public class ShopRestControllerImpl implements ShopRestController {

    private final ShopServiceImpl shopService;

    /**
     * Constructor de la clase
     * @param shopService Servicio de Shop
     */
    @Autowired
    public ShopRestControllerImpl(ShopServiceImpl shopService) {
        this.shopService = shopService;
    }

    /**
     * Método para obtener todas las tiendas
     * @return ResponseEntity con la lista de tiendas
     */
    @GetMapping
    @Override
    public ResponseEntity<List<GetShopDto>> getAllShops() {
        return ResponseEntity.ok(shopService.getAllShops());
    }

    /**
     * Método para obtener una tienda por su ID
     * @param id ID de la tienda en formato String
     * @return ResponseEntity con la tienda encontrada
     * @throws ShopNotFoundException Si no se encuentra la tienda
     */
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<GetShopDto> getShopById(@Valid @PathVariable String id) throws ShopNotFoundException {
        try {
            return ResponseEntity.ok(shopService.getShopById(id));
        } catch (ShopNotValidUUIDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // UUID no válido
        }
    }

    /**
     * Método para crear una nueva tienda
     * @param shopDto DTO con los datos de la tienda a crear
     * @return ResponseEntity con la tienda creada
     */
    @PostMapping
    @Override
    public ResponseEntity<GetShopDto> createShop(@Valid @RequestBody CreateShopDto shopDto) {
        GetShopDto newShop = shopService.createShop(shopDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newShop);
    }

    /**
     * Método para actualizar una tienda
     * @param id ID de la tienda a actualizar
     * @param shopDto DTO con los datos actualizados
     * @return ResponseEntity con la tienda actualizada
     * @throws ShopNotFoundException Si no se encuentra la tienda
     */
    @PutMapping("/{id}")
    @Override
    public ResponseEntity<GetShopDto> updateShop(@Valid @PathVariable String id, @Valid @RequestBody UpdateShopDto shopDto)
            throws ShopNotFoundException {
        try {
            return ResponseEntity.ok(shopService.updateShop(id, shopDto));
        } catch (ShopNotValidUUIDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // UUID no válido
        }
    }

    /**
     * Método para eliminar una tienda
     * @param id ID de la tienda a eliminar
     * @return ResponseEntity sin contenido
     * @throws ShopNotFoundException Si no se encuentra la tienda
     */
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<String> deleteShop(@Valid @PathVariable String id) throws ShopNotFoundException {
        try {
            shopService.deleteShop(id);
            return ResponseEntity.noContent().build();
        } catch (ShopNotValidUUIDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // UUID no válido
        }
    }
}
