package com.nullers.restbookstore.rest.shop.services;

import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.book.exceptions.BookNotValidIDException;
import com.nullers.restbookstore.rest.publisher.exceptions.PublisherIDNotValid;
import com.nullers.restbookstore.rest.publisher.exceptions.PublisherNotFound;
import com.nullers.restbookstore.rest.shop.dto.CreateShopDto;
import com.nullers.restbookstore.rest.shop.dto.GetShopDto;
import com.nullers.restbookstore.rest.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.rest.shop.exceptions.ShopNotFoundException;

import java.util.UUID;

/**
 * Interfaz para definir los servicios relacionados con las tiendas.
 * Incluye operaciones para obtener, crear, actualizar y eliminar tiendas,
 * así como para añadir y quitar libros y clientes de las tiendas.
 *
 * @author alexdor00
 */
public interface ShopService {

    /**
     * Obtiene una tienda por su ID.
     *
     * @param id El ID de la tienda a obtener.
     * @return GetShopDto que representa la tienda.
     * @throws ShopNotFoundException si la tienda no se encuentra.
     */
    GetShopDto getShopById(UUID id) throws ShopNotFoundException;

    /**
     * Crea una nueva tienda.
     *
     * @param shopDto DTO con la información para crear una nueva tienda.
     * @return GetShopDto que representa la tienda recién creada.
     */
    GetShopDto createShop(CreateShopDto shopDto);

    /**
     * Actualiza una tienda existente.
     *
     * @param id El ID de la tienda a actualizar.
     * @param shopDto DTO con la información para actualizar la tienda.
     * @return GetShopDto que representa la tienda actualizada.
     */
    GetShopDto updateShop(UUID id, UpdateShopDto shopDto);

    /**
     * Elimina una tienda por su ID.
     *
     * @param id El ID de la tienda a eliminar.
     * @throws ShopNotFoundException si la tienda no se encuentra.
     */
    void deleteShop(UUID id) throws ShopNotFoundException;

    /**
     * Añade un libro a una tienda.
     *
     * @param shopId El ID de la tienda.
     * @param bookId El ID del libro a añadir.
     * @return GetShopDto que representa la tienda después de añadir el libro.
     */
    GetShopDto addBookToShop(UUID shopId, Long bookId);

    /**
     * Quita un libro de una tienda.
     *
     * @param shopId El ID de la tienda.
     * @param bookId El ID del libro a quitar.
     * @return GetShopDto que representa la tienda después de quitar el libro.
     */
    GetShopDto removeBookFromShop(UUID shopId, Long bookId);

    /**
     * Añade un cliente a una tienda.
     *
     * @param shopId El ID de la tienda.
     * @param clientId El ID del cliente a añadir.
     * @return GetShopDto que representa la tienda después de añadir el cliente.
     */
    GetShopDto addClientToShop(UUID shopId, UUID clientId);

    /**
     * Quita un cliente de una tienda.
     *
     * @param shopId El ID de la tienda.
     * @param clientId El ID del cliente a quitar.
     * @return GetShopDto que representa la tienda después de quitar el cliente.
     */
    GetShopDto removeClientFromShop(UUID shopId, UUID clientId);
}
