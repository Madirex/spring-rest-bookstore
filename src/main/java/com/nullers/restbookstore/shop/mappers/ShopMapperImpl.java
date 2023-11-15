package com.nullers.restbookstore.shop.mappers;


import com.nullers.restbookstore.shop.dto.CreateShopDto;
import com.nullers.restbookstore.shop.dto.GetShopDto;
import com.nullers.restbookstore.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.shop.model.Shop;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Clase ShopMapperImpl
 *
 *  @author alexdor00
 */

@Component
public class ShopMapperImpl implements ShopMapper {

    public Shop toShop(CreateShopDto dto) {
        return Shop.builder()
                .id(UUID.randomUUID())
                .name(dto.getName())
                .location(dto.getLocation())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Shop toShop(Shop shop, UpdateShopDto dto) {
        shop.setName(dto.getName());
        shop.setLocation(dto.getLocation());
        shop.setUpdatedAt(LocalDateTime.now());
        return shop;
    }

    public GetShopDto toGetShopDto(Shop shop) {
        return GetShopDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .location(shop.getLocation())
                .createdAt(shop.getCreatedAt())
                .updatedAt(shop.getUpdatedAt())
                .build();
    }

    public List<GetShopDto> toShopList(List<Shop> shops) {
        return shops.stream()
                .map(this::toGetShopDto)
                .collect(Collectors.toList());
    }
}
