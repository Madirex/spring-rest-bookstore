package com.nullers.restbookstore.rest.orders.mappers;

import com.nullers.restbookstore.rest.orders.dto.OrderCreateDto;
import com.nullers.restbookstore.rest.orders.models.Order;

public class OrderCreateMapper {

    public static Order toOrder(OrderCreateDto orderCreateDto) {
        return Order.builder()
                .userId(orderCreateDto.getUserId())
                .clientId(orderCreateDto.getClientId())
                .orderLines(orderCreateDto.getOrderLines())
                .build();
    }


}
