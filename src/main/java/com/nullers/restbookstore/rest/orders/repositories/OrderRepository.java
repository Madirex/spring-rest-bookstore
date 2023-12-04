package com.nullers.restbookstore.rest.orders.repositories;

import com.nullers.restbookstore.rest.orders.models.Order;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Interfaz de OrderRepository
 */
@Repository
public interface OrderRepository extends MongoRepository<Order, ObjectId> {
    /**
     * Método que devuelve los pedidos por el ID del cliente
     *
     * @param clientId id del cliente
     * @param pageable paginación
     * @return pedidos por el ID del cliente
     */
    Page<Order> findByClientId(UUID clientId, Pageable pageable);

    /**
     * Método que devuelve los pedidos por el ID del cliente
     *
     * @param clientId id del cliente
     * @return pedidos por el ID del cliente
     */
    List<Order> findOrderIdsByClientId(UUID clientId);

    /**
     * Método que devuelve los pedidos por el ID del usuario
     *
     * @param userId   id del usuario
     * @param pageable paginación
     * @return pedidos por el ID del usuario
     */
    Page<Order> findByUserId(UUID userId, Pageable pageable);

    /**
     * Método que devuelve los pedidos por el ID de la tienda
     *
     * @param shopId   id de la tienda
     * @param pageable paginación
     * @return pedidos por el ID de la tienda
     */
    Page<Order> findByShopId(UUID shopId, Pageable pageable);

    /**
     * Método que comprueba si existe un pedido por el ID del usuario
     *
     * @param userId id del usuario
     * @return true si existe, false si no existe
     */
    boolean existsByUserId(UUID userId);
}
