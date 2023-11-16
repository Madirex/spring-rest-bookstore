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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;
    private final ShopMapperImpl shopMapper;

    @Autowired
    public ShopServiceImpl(ShopRepository shopRepository, ShopMapperImpl shopMapper) {
        this.shopRepository = shopRepository;
        this.shopMapper = shopMapper;
    }

    @Cacheable("shops")
    @Override
    public Page<GetShopDto> getAllShops(Pageable pageable) {
        return shopRepository.findAll(pageable)
                .map(shopMapper::toGetShopDto);
    }

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

    @Override
    public GetShopDto createShop(CreateShopDto shopDto) {
        Shop shop = shopMapper.toShop(shopDto);
        shop = shopRepository.save(shop);
        return shopMapper.toGetShopDto(shop);
    }

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
