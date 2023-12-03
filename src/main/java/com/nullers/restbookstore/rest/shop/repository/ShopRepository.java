package com.nullers.restbookstore.rest.shop.repository;


import com.nullers.restbookstore.rest.shop.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Interface ShopRepository
 *
 * @author alexdor00
 */


@Repository
public interface ShopRepository extends JpaRepository<Shop, UUID>, JpaSpecificationExecutor<Shop> {


}