package com.nullers.restbookstore.rest.user.controller;

import com.nullers.restbookstore.pagination.models.PageResponse;
import com.nullers.restbookstore.pagination.util.PaginationLinksUtils;
import com.nullers.restbookstore.rest.common.PageableRequest;
import com.nullers.restbookstore.rest.common.PageableUtil;
import com.nullers.restbookstore.rest.orders.dto.OrderCreateDto;
import com.nullers.restbookstore.rest.orders.models.Order;
import com.nullers.restbookstore.rest.orders.services.OrderService;
import com.nullers.restbookstore.rest.user.dto.UserInfoResponse;
import com.nullers.restbookstore.rest.user.dto.UserRequest;
import com.nullers.restbookstore.rest.user.dto.UserResponse;
import com.nullers.restbookstore.rest.user.models.User;
import com.nullers.restbookstore.rest.user.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;
import java.util.UUID;

/**
 * Controlador de usuarios
 *
 * @Author Binwei Wang
 */
@RestController
@Slf4j
@RequestMapping("/api/users")
@PreAuthorize("hasRole('USER')")
public class UserController {
    /**
     * Servicio de usuarios
     */
    private final UserService usersService;
    private final OrderService orderService;
    private final PaginationLinksUtils paginationLinksUtils;

    /**
     * Constructor de la clase
     *
     * @param userService          Servicio de usuarios
     * @param orderService         Order service
     * @param paginationLinksUtils Utilidad para crear los links de paginación
     */
    @Autowired
    public UserController(UserService userService, OrderService orderService, PaginationLinksUtils paginationLinksUtils) {
        this.usersService = userService;
        this.orderService = orderService;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    /**
     * Obtiene todos los usuarios
     *
     * @param username        Nombre de usuario
     * @param email           Email del usuario
     * @param isDeleted       Si el usuario está borrado
     * @param pageableRequest Objeto PageableRequest con los parámetros de paginación
     * @return Lista de usuarios
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<UserResponse>> findAll(
            @RequestParam(required = false) Optional<String> username,
            @RequestParam(required = false) Optional<String> email,
            @RequestParam(required = false) Optional<Boolean> isDeleted,
            @Valid PageableRequest pageableRequest,
            HttpServletRequest request
    ) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<UserResponse> pageResult = usersService.findAll(username, email, isDeleted, PageRequest.of(
                pageableRequest.getPage(), pageableRequest.getSize(),
                PageableUtil.getSort(pageableRequest)));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, pageableRequest.getOrderBy(), pageableRequest.getOrder()));
    }

    /**
     * Obtiene un usuario por su id
     *
     * @param id Id del usuario
     * @return Usuario
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserInfoResponse> findById(@PathVariable UUID id) {
        log.info("findById: id: {}", id);
        return ResponseEntity.ok(usersService.findById(id));
    }

    /**
     * Crea un usuario
     *
     * @param userRequest Usuario a crear
     * @return Usuario creado
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("save: userRequest: {}", userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(usersService.save(userRequest));
    }

    /**
     * Actualiza un usuario
     *
     * @param id          Id del usuario
     * @param userRequest Usuario a actualizar
     * @return Usuario actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id, @Valid @RequestBody UserRequest userRequest) {
        log.info("update: id: {}, userRequest: {}", id, userRequest);
        return ResponseEntity.ok(usersService.update(id, userRequest));
    }

    /**
     * Borra un usuario
     *
     * @param id Id del usuario
     * @return Respuesta vacía
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        log.info("delete: id: {}", id);
        usersService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Actualiza un usuario parcialmente
     *
     * @param id          Id del usuario a actualizar
     * @param userRequest Usuario a actualizar parcialmente
     * @return Usuario actualizado parcialmente
     */

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> patchUser(@PathVariable UUID id, @RequestBody UserRequest userRequest) {
        log.info("patch: id: {}, userRequest: {}", id, userRequest);
        return ResponseEntity.ok(usersService.update(id, userRequest));
    }


    /**
     * Obtiene el usuario autenticado
     *
     * @param user Usuario autenticado
     * @return Usuario autenticado
     */
    @GetMapping("/me/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserInfoResponse> me(@AuthenticationPrincipal User user) {
        log.info("Obteniendo usuario");
        return ResponseEntity.ok(usersService.findById(user.getId()));
    }

    /**
     * Actualiza el usuario autenticado
     *
     * @param user        Usuario autenticado
     * @param userRequest Usuario a actualizar
     * @return Usuario actualizado
     */
    @PutMapping("/me/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserResponse> updateMe(User user, @Valid @RequestBody UserRequest userRequest) {
        log.info("updateMe: user: {}, userRequest: {}", user, userRequest);
        return ResponseEntity.ok(usersService.update(user.getId(), userRequest));
    }

    /**
     * Borra el usuario autenticado
     *
     * @param user Usuario autenticado
     * @return Respuesta vacía
     */
    @DeleteMapping("/me/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteMe(User user) {
        log.info("deleteMe: user: {}", user);
        usersService.deleteById(user.getId());
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene los pedidos del usuario autenticado
     *
     * @param user            Usuario autenticado
     * @param pageableRequest Objeto PageableRequest con los parámetros de paginación
     * @return Pedidos del usuario autenticado
     */

    @GetMapping("/me/orders")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageResponse<Order>> getOrdersByUser(
            @AuthenticationPrincipal User user,
            @Valid PageableRequest pageableRequest
    ) {
        Pageable pageable = PageRequest.of(pageableRequest.getPage(), pageableRequest.getSize(),
                PageableUtil.getSort(pageableRequest));
        return ResponseEntity.ok(PageResponse.of(orderService.getOrdersByClientId(user.getId(), pageable),
                pageableRequest.getOrderBy(), pageableRequest.getOrder()));
    }

    /**
     * Obtiene un pedido del usuario autenticado
     *
     * @param user    Usuario autenticado
     * @param orderId Id del pedido
     * @return Pedido del usuario autenticado
     */
    @GetMapping("/me/orders/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Order> getOrder(
            @AuthenticationPrincipal User user,
            @PathVariable("id") ObjectId orderId
    ) {
        Order order = orderService.getOrderById(orderId);
        if (!order.getClientId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    /**
     * Crea un pedido para el usuario autenticado
     *
     * @param user  Usuario autenticado
     * @param order Pedido a crear
     * @return Pedido creado para el usuario autenticado
     */
    @PostMapping("/me/orders")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Order> createOrder(@AuthenticationPrincipal User user, @Valid @RequestBody OrderCreateDto order) {
        order.setClientId(user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(order));
    }

    /**
     * Actualiza un pedido del usuario autenticado
     *
     * @param user    Usuario autenticado
     * @param orderId Id del pedido
     * @param order   Pedido a actualizar
     * @return Pedido actualizado
     */
    @PutMapping("/me/orders/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Order> updateOrder(
            @AuthenticationPrincipal User user,
            @PathVariable("id") ObjectId orderId,
            @Valid @RequestBody OrderCreateDto order) {
        order.setClientId(user.getId());
        return ResponseEntity.ok(orderService.updateOrder(orderId, order));
    }

    /**
     * Borra un pedido del usuario autenticado
     *
     * @param user    Usuario autenticado
     * @param orderId Id del pedido
     * @return Respuesta vacía
     */
    @DeleteMapping("/me/orders/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteOrder(
            @AuthenticationPrincipal User user,
            @PathVariable("id") ObjectId orderId) {
        Order order = orderService.getOrderById(orderId);
        if (!order.getClientId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
