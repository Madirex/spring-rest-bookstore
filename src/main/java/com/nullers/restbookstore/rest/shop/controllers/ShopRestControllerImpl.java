package com.nullers.restbookstore.rest.shop.controllers;

import com.nullers.restbookstore.rest.shop.dto.CreateShopDto;
import com.nullers.restbookstore.rest.shop.dto.GetShopDto;
import com.nullers.restbookstore.rest.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.rest.shop.services.ShopServiceImpl;
import com.nullers.restbookstore.rest.shop.exceptions.ShopNotFoundException;
import com.nullers.restbookstore.rest.shop.exceptions.ShopNotValidUUIDException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Implementación del controlador REST para la gestión de tiendas (Shops).
 * Provee endpoints para operaciones CRUD sobre tiendas.
 *
 * @author alexdor00
 */
@RestController
@RequestMapping("/api/shops")
public class ShopRestControllerImpl implements ShopRestController {

    private final ShopServiceImpl shopService;

    /**
     * Constructor que inyecta el servicio de Shop.
     * @param shopService Servicio de Shop para operaciones de negocio.
     */
    @Autowired
    public ShopRestControllerImpl(ShopServiceImpl shopService) {
        this.shopService = shopService;
    }

    /**
     * Obtiene todas las tiendas disponibles.
     * @return ResponseEntity con una lista de todas las tiendas en formato DTO.
     */
    @GetMapping
    @Override
    public ResponseEntity<List<GetShopDto>> getAllShops() {
        return ResponseEntity.ok(shopService.getAllShops());
    }

    /**
     * Obtiene una tienda específica por su ID.
     * @param id ID de la tienda en formato String.
     * @return ResponseEntity con los detalles de la tienda en formato DTO.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
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
     * Crea una nueva tienda con los datos proporcionados.
     * @param shopDto DTO con la información de la tienda a crear.
     * @return ResponseEntity con los detalles de la tienda creada en formato DTO.
     */
    @PostMapping
    @Override
    public ResponseEntity<GetShopDto> createShop(@Valid @RequestBody CreateShopDto shopDto) {
        GetShopDto newShop = shopService.createShop(shopDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newShop);
    }

    /**
     * Actualiza una tienda existente con los datos proporcionados.
     * @param id ID de la tienda a actualizar.
     * @param shopDto DTO con los datos actualizados de la tienda.
     * @return ResponseEntity con los detalles de la tienda actualizada en formato DTO.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
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
     * Elimina una tienda específica por su ID.
     * @param id ID de la tienda a eliminar.
     * @return ResponseEntity sin contenido indicando que la tienda ha sido eliminada.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
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
