package com.nullers.restbookstore.rest.orders.services;

import com.nullers.restbookstore.rest.orders.dto.OrderCreateDto;
import com.nullers.restbookstore.rest.orders.exceptions.OrderNotFoundException;
import com.nullers.restbookstore.rest.orders.models.Order;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Interfaz de OrderService
 */
public interface OrderService {

    /**
     * Método que devuelve todos los pedidos
     *
     * @param pageable paginación
     * @return todos los pedidos
     */
    Page<Order> getAllOrders(Pageable pageable);

    /**
     * Método que devuelve un pedido por el ID
     *
     * @param id id del pedido
     * @return pedido por el ID
     */
    Order getOrderById(ObjectId id);

    /**
     * Método que crea un pedido
     *
     * @param order pedido
     * @return pedido creado
     */
    Order createOrder(OrderCreateDto order);

    /**
     * Método que actualiza un pedido por el ID
     *
     * @param id    id del pedido
     * @param order pedido
     * @return pedido actualizado
     */
    Order updateOrder(ObjectId id, OrderCreateDto order);

    /**
     * Método que elimina un pedido por el ID
     *
     * @param id id del pedido
     * @throws OrderNotFoundException excepción si no existe el pedido
     */
    void deleteOrder(ObjectId id) throws OrderNotFoundException;

    /**
     * Método que devuelve los pedidos de un usuario por el ID del usuario
     *
     * @param userId   id del usuario
     * @param pageable paginación
     * @return pedidos de un usuario por el ID del usuario
     */
    Page<Order> getOrdersByUserId(UUID userId, Pageable pageable);

    /**
     * Método que devuelve los pedidos de un usuario por el ID del cliente
     *
     * @param clientId id del cliente
     * @param pageable paginación
     * @return pedidos de un usuario por el ID del cliente
     */
    Page<Order> getOrdersByClientId(UUID clientId, Pageable pageable);

    /**
     * Método que devuelve los pedidos de un usuario por el ID de la tienda
     *
     * @param shopId   id de la tienda
     * @param pageable paginación
     * @return pedidos de un usuario por el ID de la tienda
     */
    Page<Order> getOrdersByShopId(UUID shopId, Pageable pageable);

    /**
     * Método que comprueba si existe un pedido por el ID del usuario
     *
     * @param userId id del usuario
     * @return true si existe, false si no
     */
    boolean existsByUserId(UUID userId);

    /**
     * Método que elimina lógicamente un pedido
     *
     * @param id id del pedido
     * @return pedido eliminada
     */
    Order deleteLogicOrder(ObjectId id);
}
