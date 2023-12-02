package com.nullers.restbookstore.rest.orders.repositories;

import com.nullers.restbookstore.rest.orders.models.Order;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends MongoRepository<Order, ObjectId> {
    Page<Order> findByClientId(UUID clientId, Pageable pageable);
    List<Order> findOrderIdsByClientId(UUID clientId);

    Page<Order> findByUserId(UUID userId, Pageable pageable);

    Page<Order> findByShopId(UUID shopId, Pageable pageable);

    boolean existsByUserId(UUID userId);
}
