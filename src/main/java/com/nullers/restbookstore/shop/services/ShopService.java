package com.nullers.restbookstore.shop.services;

import com.nullers.restbookstore.shop.dto.CreateShopDto;
import com.nullers.restbookstore.shop.dto.GetShopDto;
import com.nullers.restbookstore.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.shop.exceptions.ShopNotFoundException;
import com.nullers.restbookstore.shop.exceptions.ShopNotValidUUIDException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Clase ShopService
 *
 * @author alexdor00
 */
public interface ShopService {

    /**
     * Obtiene una página de tiendas.
     * @param pageable Contiene la información de paginación y ordenación.
     * @return Una página de tiendas en formato GetShopDto.
     */
    Page<GetShopDto> getAllShops(Pageable pageable);

    /**
     * Obtiene una tienda específica por su identificador.
     * @param id El identificador de la tienda.
     * @return Un GetShopDto con los detalles de la tienda.
     * @throws ShopNotValidUUIDException Si el identificador no es un UUID válido.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     */
    GetShopDto getShopById(String id) throws ShopNotValidUUIDException, ShopNotFoundException;

    /**
     * Crea una nueva tienda con la información proporcionada.
     * @param shopDto Información de la nueva tienda.
     * @return Un GetShopDto con los detalles de la tienda creada.
     */
    GetShopDto createShop(CreateShopDto shopDto);

    /**
     * Actualiza una tienda existente con la información proporcionada.
     * @param id El identificador de la tienda a actualizar.
     * @param shopDto Información actualizada de la tienda.
     * @return Un GetShopDto con los detalles de la tienda actualizada.
     * @throws ShopNotValidUUIDException Si el identificador no es un UUID válido.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     */
    GetShopDto updateShop(String id, UpdateShopDto shopDto) throws ShopNotValidUUIDException, ShopNotFoundException;

    /**
     * Elimina una tienda por su identificador.
     * @param id El identificador de la tienda a eliminar.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     * @throws ShopNotValidUUIDException Si el identificador no es un UUID válido.
     */
    void deleteShop(String id) throws ShopNotFoundException, ShopNotValidUUIDException;
}
