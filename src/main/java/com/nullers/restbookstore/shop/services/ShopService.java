package com.nullers.restbookstore.shop.services;

import com.nullers.restbookstore.shop.dto.CreateShopDto;
import com.nullers.restbookstore.shop.dto.GetShopDto;
import com.nullers.restbookstore.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.shop.exceptions.ShopNotFoundException;
import com.nullers.restbookstore.shop.exceptions.ShopNotValidUUIDException;

import java.util.List;

/**
 * Interfaz ShopService
 * @author alexdor00
 */
public interface ShopService {
    /**
     * Obtiene una lista de todas las tiendas.
     * @return Lista de GetShopDto con la información de las tiendas.
     */
    List<GetShopDto> getAllShops();

    /**
     * Obtiene los detalles de una tienda específica por su identificador.
     * @param id El identificador de la tienda.
     * @return GetShopDto con los detalles de la tienda.
     * @throws ShopNotValidUUIDException Si el identificador proporcionado no es válido.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     */
    GetShopDto getShopById(String id) throws ShopNotValidUUIDException, ShopNotFoundException;

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
    GetShopDto updateShop(String id, UpdateShopDto shopDto) throws ShopNotValidUUIDException, ShopNotFoundException;

    /**
     * Elimina una tienda por su identificador.
     * @param id El identificador de la tienda a eliminar.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     * @throws ShopNotValidUUIDException Si el identificador proporcionado no es válido.
     */
    void deleteShop(String id) throws ShopNotFoundException, ShopNotValidUUIDException;


}
