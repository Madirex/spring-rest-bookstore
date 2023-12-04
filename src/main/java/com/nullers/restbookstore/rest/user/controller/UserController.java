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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Obtiene todos los usuarios", description = "Obtiene una lista de usuarios")
    @Parameter(name = "username", description = "Nombre de usuario", example = "usuario1")
    @Parameter(name = "email", description = "Email del usuario", example = "contact@usermail@.com")
    @Parameter(name = "isDeleted", description = "Usuario borrado o no", example = "true")
    @Parameter(name = "page", description = "Número de página", example = "0")
    @Parameter(name = "size", description = "Tamaño de la página", example = "10")
    @Parameter(name = "orderBy", description = "Campo de ordenación", example = "id")
    @Parameter(name = "order", description = "Dirección de ordenación", example = "asc")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de usuarios"),
            @ApiResponse(responseCode = "400", description = "Petición de usuarios no válida")
    })
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
    @Operation(summary = "Obtiene un usuario dado un id", description = "Obtiene un usuario dado un id")
    @Parameter(name = "id", description = "id del usuario", example = "660e8400-e29b-41d4-a716-446655440000")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
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
    @Operation(summary = "Crea un usuario", description = "Crea un usuario")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Usuario a crear", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado"),
            @ApiResponse(responseCode = "400", description = "Usuario no válido")
    })
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
    @Operation(summary = "Actualiza un usuario", description = "Actualiza un usuario")
    @Parameter(name = "id", description = "id del usuario a actualizar", example = "660e8400-e29b-41d4-a716-446655440000")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Usuario actualizado", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
            @ApiResponse(responseCode = "400", description = "Usuario no válido"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
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
    @Operation(summary = "Elimina un usuario", description = "Elimina un usuario")
    @Parameter(name = "id", description = "Id del usuario a eliminar", example = "660e8400-e29b-41d4-a716-446655440000", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario borrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
    })
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
    @Operation(summary = "Actualiza un usuario parcialmente", description = "Actualiza un usuario parcialmente")
    @Parameter(name = "id", description = "id del usuario a actualizar", example = "660e8400-e29b-41d4-a716-446655440000")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Usuario actualizado", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
            @ApiResponse(responseCode = "400", description = "Usuario no válido"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
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
    @Operation(summary = "Consulta la información del perfil que ha iniciado sesión", description = "Consulta la información del perfil que ha iniciado sesión")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Usuario a consultar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario consultado"),
            @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    })
    @GetMapping("/me/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserInfoResponse> me(@AuthenticationPrincipal User user) {
        log.info("Obteniendo usuario");
        return ResponseEntity.ok(usersService.findById(user.getId()));
    }

    /**
     * Obtiene los pedidos del usuario autenticado
     *
     * @param user            Usuario autenticado
     * @param pageableRequest Objeto PageableRequest con los parámetros de paginación
     * @return Pedidos del usuario autenticado
     */
    @Operation(summary = "Obtiene los pedidos del perfil que ha iniciado sesión", description = "Obtiene los pedidos del perfil que ha iniciado sesión")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Usuario a consultar pedidos", required = true)
    @Parameter(name = "page", description = "Número de página", example = "0")
    @Parameter(name = "size", description = "Tamaño de la página", example = "10")
    @Parameter(name = "orderBy", description = "Campo de ordenación", example = "id")
    @Parameter(name = "order", description = "Dirección de ordenación", example = "asc")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario consultado"),
            @ApiResponse(responseCode = "400", description = "Datos de consulta no válidos"),
            @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    })
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
    @Operation(summary = "Obtiene un pedido del usuario autenticado dado su ID", description = "Obtiene un pedido del usuario autenticado dado su ID")
    @Parameter(name = "id", description = "ID del pedido", example = "660e8400-e29b-41d4-a716-446655440000")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido de usuario consultado"),
            @ApiResponse(responseCode = "400", description = "ID de pedido no válido"),
            @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    })
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
    @Operation(summary = "Crea un pedido dado el usuario que ha iniciado sesión", description = "Crea un pedido dado el usuario que ha iniciado sesión")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Pedido a crear", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido de usuario consultado"),
            @ApiResponse(responseCode = "400", description = "ID de pedido no válido"),
            @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    })
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
    @Operation(summary = "Actualiza un pedido dado el usuario que ha iniciado sesión", description = "Actualiza un pedido dado el usuario que ha iniciado sesión")
    @Parameter(name = "id", description = "ID del pedido", example = "660e8400-e29b-41d4-a716-446655440000")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Pedido a actualizar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido de usuario actualizado"),
            @ApiResponse(responseCode = "400", description = "ID de pedido no válido"),
            @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    })
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
    @Operation(summary = "Elimina un pedido dado el usuario que ha iniciado sesión", description = "Elimina un pedido dado el usuario que ha iniciado sesión")
    @Parameter(name = "id", description = "ID del pedido", example = "660e8400-e29b-41d4-a716-446655440000")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido de usuario eliminado"),
            @ApiResponse(responseCode = "400", description = "ID de pedido no válido"),
            @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    })
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
