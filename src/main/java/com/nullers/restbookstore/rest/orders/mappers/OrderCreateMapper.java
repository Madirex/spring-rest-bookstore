package com.nullers.restbookstore.rest.orders.mappers;

import com.nullers.restbookstore.rest.orders.dto.OrderCreateDto;
import com.nullers.restbookstore.rest.orders.models.Order;

/**
 * Clase para mapear un OrderCreateDto a Order
 */
public class OrderCreateMapper {

    /**
     * Constructor privado para evitar instancias
     */
    private OrderCreateMapper() {
        // Constructor privado para evitar instancias
    }

    /**
     * Método que convierte un OrderCreateDto a Order
     *
     * @param orderCreateDto dto con los datos de OrderCreateDto a mapear
     * @return Order mapeado
     */
    public static Order toOrder(OrderCreateDto orderCreateDto) {
        return Order.builder()
                .userId(orderCreateDto.getUserId())
                .clientId(orderCreateDto.getClientId())
                .orderLines(orderCreateDto.getOrderLines())
                .shopId(orderCreateDto.getShopId())
                .build();
    }

}
