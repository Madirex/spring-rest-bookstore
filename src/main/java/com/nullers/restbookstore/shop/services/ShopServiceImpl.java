package com.nullers.restbookstore.shop.services;

import com.nullers.restbookstore.shop.dto.CreateShopDto;
import com.nullers.restbookstore.shop.dto.GetShopDto;
import com.nullers.restbookstore.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.shop.exceptions.ShopNotFoundException;
import com.nullers.restbookstore.shop.exceptions.ShopNotValidUUIDException;
import com.nullers.restbookstore.shop.mappers.ShopMapperImpl;
import com.nullers.restbookstore.shop.model.Shop;
import com.nullers.restbookstore.shop.repositories.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Servicio que implementa las operaciones de negocio para la entidad Shop.
 * Utiliza ShopRepository para acceso a datos y ShopMapper para la conversión entre entidades y DTOs.
 *
 * @author alexdor00
 */
@Service
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;
    private final ShopMapperImpl shopMapper;

    /**
     * Constructor que inyecta el repositorio de tiendas y el mapper.
     * @param shopRepository Repositorio para las operaciones de base de datos de Shop.
     * @param shopMapper Mapper para convertir entre Shop y sus DTOs.
     */
    @Autowired
    public ShopServiceImpl(ShopRepository shopRepository, ShopMapperImpl shopMapper) {
        this.shopRepository = shopRepository;
        this.shopMapper = shopMapper;
    }

    /**
     * Obtiene todas las tiendas y las convierte a DTOs.
     * Los resultados se almacenan en caché para mejorar el rendimiento.
     * @return Lista de tiendas en forma de DTOs.
     */
    @Cacheable("shops")
    @Override
    public List<GetShopDto> getAllShops() {
        return shopRepository.findAll().stream()
                .map(shopMapper::toGetShopDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una tienda por su UUID y la convierte a DTO.
     * @param id Identificador UUID de la tienda.
     * @return DTO de la tienda encontrada.
     * @throws ShopNotValidUUIDException Si el UUID proporcionado no es válido.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     */
    @Override
    public GetShopDto getShopById(String id) throws ShopNotValidUUIDException, ShopNotFoundException {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new ShopNotValidUUIDException("UUID no válido: " + id);
        }

        Shop shop = shopRepository.findById(uuid)
                .orElseThrow(() -> new ShopNotFoundException("Tienda no encontrada con ID: " + id));
        return shopMapper.toGetShopDto(shop);
    }

    /**
     * Crea una nueva tienda a partir de un DTO y la guarda en la base de datos.
     * @param shopDto DTO con los datos para crear la tienda.
     * @return DTO de la tienda creada.
     */
    @Override
    public GetShopDto createShop(CreateShopDto shopDto) {
        Shop shop = shopMapper.toShop(shopDto);
        shop = shopRepository.save(shop);
        return shopMapper.toGetShopDto(shop);
    }

    /**
     * Actualiza una tienda existente con los datos proporcionados en el DTO.
     * @param id Identificador UUID de la tienda a actualizar.
     * @param shopDto DTO con los datos actualizados.
     * @return DTO de la tienda actualizada.
     * @throws ShopNotValidUUIDException Si el UUID proporcionado no es válido.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     */
    @Override
    public GetShopDto updateShop(String id, UpdateShopDto shopDto) throws ShopNotValidUUIDException, ShopNotFoundException {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new ShopNotValidUUIDException("UUID no válido: " + id);
        }

        Shop shop = shopRepository.findById(uuid)
                .orElseThrow(() -> new ShopNotFoundException("Tienda no encontrada con ID: " + id));
        shop = shopMapper.toShop(shop, shopDto);
        shop = shopRepository.save(shop);
        return shopMapper.toGetShopDto(shop);
    }

    /**
     * Elimina una tienda de la base de datos por su UUID y evita la caché relacionada.
     * @param id Identificador UUID de la tienda a eliminar.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     * @throws ShopNotValidUUIDException Si el UUID proporcionado no es válido.
     */
    @CacheEvict(value = "shops", allEntries = true)
    @Override
    public void deleteShop(String id) throws ShopNotFoundException, ShopNotValidUUIDException {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new ShopNotValidUUIDException("UUID no válido: " + id);
        }

        Shop shop = shopRepository.findById(uuid)
                .orElseThrow(() -> new ShopNotFoundException("Tienda no encontrada con ID: " + id));
        shopRepository.delete(shop);
    }

}
