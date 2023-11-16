package com.nullers.restbookstore.rest.shop.controllers;

import com.nullers.restbookstore.rest.shop.dto.CreateShopDto;
import com.nullers.restbookstore.rest.shop.dto.GetShopDto;
import com.nullers.restbookstore.rest.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.rest.shop.exceptions.ShopNotFoundException;
import com.nullers.restbookstore.rest.shop.exceptions.ShopNotValidUUIDException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Interfaz para el controlador REST de Shop, definiendo los endpoints para la gestión de tiendas.
 * Incluye operaciones para obtener, crear, actualizar y eliminar tiendas.
 *
 * @author alexdor00
 */
public interface ShopRestController {
    /**
     * Obtiene todas las tiendas disponibles.
     * @return ResponseEntity con una lista de tiendas en formato DTO.
     */
    ResponseEntity<List<GetShopDto>> getAllShops();

    /**
     * Obtiene una tienda específica por su identificador.
     * @param id Identificador de la tienda.
     * @return ResponseEntity con la tienda encontrada en formato DTO.
     * @throws ShopNotValidUUIDException Si el identificador proporcionado no es válido.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     */
    ResponseEntity<GetShopDto> getShopById(@Valid @PathVariable String id)
            throws ShopNotValidUUIDException, ShopNotFoundException;

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
     * @throws ShopNotValidUUIDException Si el identificador proporcionado no es válido.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     */
    ResponseEntity<GetShopDto> updateShop(@Valid @PathVariable String id, @Valid @RequestBody UpdateShopDto shopDto)
            throws ShopNotValidUUIDException, ShopNotFoundException;

    /**
     * Elimina una tienda específica por su identificador.
     * @param id Identificador de la tienda a eliminar.
     * @return ResponseEntity indicando el resultado de la operación.
     * @throws ShopNotValidUUIDException Si el identificador proporcionado no es válido.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     */
    ResponseEntity<String> deleteShop(@Valid @PathVariable String id) throws ShopNotValidUUIDException, ShopNotFoundException;
}
