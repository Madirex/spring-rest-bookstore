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
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public List<GetShopDto> getAllShops() {
        return shopRepository.findAll().stream()
                .map(shopMapper::toGetShopDto)
                .collect(Collectors.toList());
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
