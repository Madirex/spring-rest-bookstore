package com.nullers.restbookstore.rest.orders.mapper;

import com.nullers.restbookstore.rest.orders.dto.OrderCreateDto;
import com.nullers.restbookstore.rest.orders.mappers.OrderCreateMapper;
import com.nullers.restbookstore.rest.orders.models.Order;
import com.nullers.restbookstore.rest.orders.models.OrderLine;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class OrderCreateMapperTest {

    OrderCreateDto orderCreateDto = OrderCreateDto.builder()
            .userId(UUID.randomUUID())
            .clientId(UUID.randomUUID())
            .orderLines(List.of(
                    OrderLine.builder()
                            .bookId(1L)
                            .quantity(1)
                            .build(),
                    OrderLine.builder()
                            .bookId(2L)
                            .quantity(2)
                            .build()
            ))
            .build();


    @Test
    public void toOrder() {
        Order order = OrderCreateMapper.toOrder(orderCreateDto);

        assertAll(
                () -> assertEquals(orderCreateDto.getUserId(), order.getUserId()),
                () -> assertEquals(orderCreateDto.getClientId(), order.getClientId()),
                () -> assertEquals(orderCreateDto.getOrderLines(), order.getOrderLines())
        );

    }
}
