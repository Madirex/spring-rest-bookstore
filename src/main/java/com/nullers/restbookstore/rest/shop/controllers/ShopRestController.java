package com.nullers.restbookstore.rest.shop.controllers;

import com.nullers.restbookstore.rest.shop.dto.CreateShopDto;
import com.nullers.restbookstore.rest.shop.dto.GetShopDto;
import com.nullers.restbookstore.rest.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.rest.shop.exceptions.ShopNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

/**
 * Interfaz para el controlador REST de Shop, definiendo los endpoints para la gestión de tiendas.
 * Incluye operaciones para obtener, crear, actualizar y eliminar tiendas.
 *
 * @author alexdor00
 */
public interface ShopRestController {

    /**
     * Obtiene una tienda específica por su identificador.
     * @param id Identificador de la tienda.
     * @return ResponseEntity con la tienda encontrada en formato DTO.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     */
    ResponseEntity<GetShopDto> getShopById(@Valid @PathVariable UUID id)
            throws  ShopNotFoundException;

    /**
     * Crea una nueva tienda basada en la información proporcionada.
     * @param shopDto DTO con la información para crear la tienda.
     * @return ResponseEntity con la tienda creada en formato DTO.
     */
    ResponseEntity<GetShopDto> createShop(@Valid @RequestBody CreateShopDto shopDto);

    /**
     * Actualiza una tienda existente con la información proporcionada.
     * @param id Identificador de la tienda a actualizar.
     * @param shopDto DTO con la información actualizada de la tienda.
     * @return ResponseEntity con la tienda actualizada en formato DTO.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     */
    ResponseEntity<GetShopDto> updateShop(@Valid @PathVariable UUID id, @Valid @RequestBody UpdateShopDto shopDto)
            throws  ShopNotFoundException;

    /**
     * Elimina una tienda específica por su identificador.
     * @param id Identificador de la tienda a eliminar.
     * @return ResponseEntity indicando el resultado de la operación.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     */
    ResponseEntity<String> deleteShop(@Valid @PathVariable UUID id);

    ResponseEntity<GetShopDto> addBookToShop(@PathVariable UUID id, @PathVariable Long bookId);

    ResponseEntity<GetShopDto> removeBookFromShop(@PathVariable UUID id, @PathVariable Long bookId);

    ResponseEntity<GetShopDto> addClientToShop(@PathVariable UUID id, @PathVariable UUID clientId);

    ResponseEntity<GetShopDto> removeClientFromShop(@PathVariable UUID id, @PathVariable UUID clientId);

}
