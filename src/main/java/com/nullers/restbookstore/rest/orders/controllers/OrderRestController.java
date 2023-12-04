package com.nullers.restbookstore.rest.orders.controllers;

import com.nullers.restbookstore.pagination.models.PageResponse;
import com.nullers.restbookstore.pagination.util.PaginationLinksUtils;
import com.nullers.restbookstore.rest.common.PageableRequest;
import com.nullers.restbookstore.rest.common.PageableUtil;
import com.nullers.restbookstore.rest.orders.dto.OrderCreateDto;
import com.nullers.restbookstore.rest.orders.models.Order;
import com.nullers.restbookstore.rest.orders.services.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

/**
 * Clase OrderRestController
 */
@RestController
@RequestMapping("/api/orders")
@PreAuthorize("hasRole('ADMIN')")
public class OrderRestController {

    private final OrderServiceImpl orderService;

    private final PaginationLinksUtils paginationLinksUtils;

    /**
     * Constructor
     *
     * @param orderService         servicio de pedido
     * @param paginationLinksUtils utilidad de paginación
     */
    @Autowired
    public OrderRestController(OrderServiceImpl orderService, PaginationLinksUtils paginationLinksUtils) {
        this.orderService = orderService;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    /**
     * Método para obtener todos los pedidos
     *
     * @param pageableRequest paginación
     * @param request         petición
     * @return ResponseEntity<PageResponse < Order>> con los pedidos
     */
    @GetMapping
    public ResponseEntity<PageResponse<Order>> getAllOrders(
            @Valid PageableRequest pageableRequest,
            HttpServletRequest request
    ) {
        Page<Order> orders = orderService.getAllOrders(
                PageRequest.of(pageableRequest.getPage(), pageableRequest.getSize(), PageableUtil.getSort(pageableRequest)));
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(orders, uriBuilder))
                .body(PageResponse.of(orders, pageableRequest.getOrderBy(), pageableRequest.getOrder()));
    }

    /**
     * Método para obtener un pedido por ID
     *
     * @param id id del pedido
     * @return ResponseEntity<Order> con el pedido
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable ObjectId id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    /**
     * Método para crear un pedido
     *
     * @param order pedido a crear
     * @return ResponseEntity<Order> con el pedido
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderCreateDto order) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(order));
    }

    /**
     * Método para actualizar un pedido
     *
     * @param id    id del pedido
     * @param order pedido a actualizar
     * @return ResponseEntity<Order> con el pedido
     */
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable ObjectId id, @Valid @RequestBody OrderCreateDto order) {
        return ResponseEntity.ok(orderService.updateOrder(id, order));
    }

    /**
     * Método para eliminar un pedido
     *
     * @param id id del pedido
     * @return ResponseEntity<Void>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable ObjectId id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Método para eliminar un pedido lógicamente
     *
     * @param id id del pedido
     * @return ResponseEntity<Order> con el pedido
     */
    @PutMapping("/delete/{id}")
    public ResponseEntity<Order> deleteLogicOrder(@PathVariable ObjectId id) {
        return ResponseEntity.ok(orderService.deleteLogicOrder(id));
    }

    /**
     * Método para obtener los pedidos de un cliente
     *
     * @param id              id del cliente
     * @param pageableRequest paginación
     * @param request         petición
     * @return ResponseEntity<PageResponse < Order>> con los pedidos
     */
    @GetMapping("/client/{id}")
    public ResponseEntity<PageResponse<Order>> getOrdersByClientId(
            @PathVariable UUID id,
            @Valid PageableRequest pageableRequest,
            HttpServletRequest request
    ) {
        Page<Order> orders = orderService.getOrdersByClientId(id,
                PageRequest.of(pageableRequest.getPage(), pageableRequest.getSize(), PageableUtil.getSort(pageableRequest)));
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(orders, uriBuilder))
                .body(PageResponse.of(orders, pageableRequest.getOrderBy(), pageableRequest.getOrder()));
    }

    /**
     * Método para obtener los pedidos de un usuario
     *
     * @param id              id del usuario
     * @param pageableRequest paginación
     * @param request         petición
     * @return ResponseEntity<PageResponse < Order>> con los pedidos
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<PageResponse<Order>> getOrdersByUserId(
            @PathVariable UUID id,
            @Valid PageableRequest pageableRequest,
            HttpServletRequest request
    ) {
        String orderBy = pageableRequest.getOrderBy();
        String order = pageableRequest.getOrder();
        Integer page = pageableRequest.getPage();
        Integer size = pageableRequest.getSize();
        Sort sort = order.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(orderBy).ascending() : Sort.by(orderBy).descending();
        Page<Order> orders = orderService.getOrdersByUserId(id, PageRequest.of(page, size, sort));
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(orders, uriBuilder))
                .body(PageResponse.of(orders, orderBy, order));
    }

    /**
     * Método para obtener los pedidos de una tienda
     *
     * @param id              id de la tienda
     * @param pageableRequest paginación
     * @param request         petición
     * @return ResponseEntity<PageResponse < Order>> con los pedidos
     */
    @GetMapping("/shop/{id}")
    public ResponseEntity<PageResponse<Order>> getOrdersByShopId(
            @PathVariable UUID id,
            @Valid PageableRequest pageableRequest,
            HttpServletRequest request
    ) {
        Page<Order> orders = orderService.getOrdersByShopId(id,
                PageRequest.of(pageableRequest.getPage(), pageableRequest.getSize(), PageableUtil.getSort(pageableRequest)));
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(orders, uriBuilder))
                .body(PageResponse.of(orders, pageableRequest.getOrderBy(), pageableRequest.getOrder()));
    }
}
