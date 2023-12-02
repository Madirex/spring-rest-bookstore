package com.nullers.restbookstore.rest.shop.services;

import com.nullers.restbookstore.rest.shop.dto.CreateShopDto;
import com.nullers.restbookstore.rest.shop.dto.GetShopDto;
import com.nullers.restbookstore.rest.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.rest.shop.exceptions.ShopNotFoundException;
import com.nullers.restbookstore.rest.shop.exceptions.ShopNotValidUUIDException;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.UUID;

/**
 * Interfaz ShopService
 * @author alexdor00
 */
public interface ShopService {

    /**
     * Obtiene los detalles de una tienda específica por su identificador.
     * @param id El identificador de la tienda.
     * @return GetShopDto con los detalles de la tienda.
     * @throws ShopNotValidUUIDException Si el identificador proporcionado no es válido.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     */
    GetShopDto getShopById(UUID id) throws ShopNotValidUUIDException, ShopNotFoundException;

    /**
     * Crea una nueva tienda basada en los datos proporcionados.
     * @param shopDto DTO con la información para crear la tienda.
     * @return GetShopDto con los detalles de la tienda creada.
     */
    GetShopDto createShop(CreateShopDto shopDto);

    /**
     * Actualiza una tienda existente con los datos proporcionados.
     * @param id El identificador de la tienda a actualizar.
     * @param shopDto DTO con los datos actualizados para la tienda.
     * @return GetShopDto con los detalles de la tienda actualizada.
     * @throws ShopNotValidUUIDException Si el identificador proporcionado no es válido.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     */
    GetShopDto updateShop(UUID id, UpdateShopDto shopDto);

    /**
     * Elimina una tienda por su identificador.
     * @param id El identificador de la tienda a eliminar.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     * @throws ShopNotValidUUIDException Si el identificador proporcionado no es válido.
     */
    void deleteShop(UUID id) throws ShopNotFoundException, ShopNotValidUUIDException;

    GetShopDto addBookToShop(UUID id, Long bookId);

    GetShopDto removeBookFromShop(UUID id, Long bookId);

    GetShopDto addClientToShop(UUID id, UUID clientId);

    GetShopDto removeClientFromShop(UUID id, UUID clientId);



}
