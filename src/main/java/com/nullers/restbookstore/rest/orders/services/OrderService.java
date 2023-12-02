package com.nullers.restbookstore.rest.orders.services;

import com.nullers.restbookstore.rest.orders.dto.OrderCreateDto;
import com.nullers.restbookstore.rest.orders.exceptions.OrderNotFoundException;
import com.nullers.restbookstore.rest.orders.models.Order;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService {

    Page<Order> getAllOrders(Pageable pageable);

    Order getOrderById(ObjectId id);

    Order createOrder(OrderCreateDto order);

    Order updateOrder(ObjectId id, OrderCreateDto order);

    void deleteOrder(ObjectId id) throws OrderNotFoundException;

    Page<Order> getOrdersByUserId(UUID userId, Pageable pageable);

    Page<Order> getOrdersByClientId(UUID clientId, Pageable pageable);

    Page<Order> getOrdersByShopId(UUID shopId, Pageable pageable);

    boolean existsByUserId(UUID userId);

    Order deleteLogicOrder(ObjectId id);





}
