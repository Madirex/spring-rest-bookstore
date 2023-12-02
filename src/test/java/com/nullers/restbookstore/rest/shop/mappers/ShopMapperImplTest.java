package com.nullers.restbookstore.rest.shop.mappers;

import com.nullers.restbookstore.rest.book.dto.UpdateBookDTO;
import com.nullers.restbookstore.rest.common.Address;
import com.nullers.restbookstore.rest.shop.dto.CreateShopDto;
import com.nullers.restbookstore.rest.shop.dto.GetShopDto;
import com.nullers.restbookstore.rest.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.rest.shop.model.Shop;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShopMapperImplTest {


    ShopMapperImpl shopMapperImpl = new ShopMapperImpl();


    CreateShopDto createShopDto = CreateShopDto.builder()
            .name("Tienda 1")
            .location(Address.builder()
                    .province("Madrid")
                    .city("Madrid")
                    .street("Calle 1")
                    .number("1")
                    .PostalCode("28001")
                    .country("España")
                    .build())
            .build();

    UpdateShopDto updateShopDto = UpdateShopDto.builder()
            .name("Tienda 2")
            .location(Address.builder()
                    .province("Madrid")
                    .city("Madrid")
                    .street("Calle 2")
                    .number("2")
                    .PostalCode("28002")
                    .country("España")
                    .build())
            .build();

    Shop shop = Shop.builder()
            .name("Tienda 1")
            .location(Address.builder()
                    .province("Madrid")
                    .city("Madrid")
                    .street("Calle 1")
                    .number("1")
                    .PostalCode("28001")
                    .country("España")
                    .build())
            .build();

    @Test
    void toShopTest(){

        Shop shop = shopMapperImpl.toShop(createShopDto);
        assertAll(
                () -> assertEquals(shop.getName(), createShopDto.getName()),
                () -> assertEquals(shop.getLocation(), createShopDto.getLocation())
        );

    }

    @Test
    void toShopTest2(){

        Shop shop = shopMapperImpl.toShop(this.shop, updateShopDto);
        assertAll(
                () -> assertEquals(shop.getName(), updateShopDto.getName()),
                () -> assertEquals(shop.getLocation(), updateShopDto.getLocation())
        );

    }

    @Test
    void toGetShopDtoTest(){

        GetShopDto shopT = shopMapperImpl.toGetShopDto(shop);
        assertAll(
                () -> assertEquals(shopT.getName(), createShopDto.getName()),
                () -> assertEquals(shopT.getLocation(), createShopDto.getLocation())
        );

    }

    @Test
    void toShopListTest(){

        List<GetShopDto> shopT = shopMapperImpl.toShopList(List.of(shop));
        assertAll(
                () -> assertEquals(shopT.get(0).getName(), shop.getName()),
                () -> assertEquals(shopT.get(0).getLocation(), shop.getLocation())
        );

    }

}
