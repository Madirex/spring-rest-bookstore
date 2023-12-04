package com.nullers.restbookstore.rest.user.controller;

import com.nullers.restbookstore.pagination.models.PageResponse;
import com.nullers.restbookstore.pagination.util.PaginationLinksUtils;
import com.nullers.restbookstore.rest.common.PageableRequest;
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
import io.swagger.v3.oas.annotations.Parameters;
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
import org.springframework.data.domain.Sort;
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
    @Parameters({
            @Parameter(name = "username", description = "Nombre de usuario", example = ""),
            @Parameter(name = "email", description = "Email del usuario", example = ""),
            @Parameter(name = "isDeleted", description = "Usuario borrado o no", example = "true"),
    })
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
        String orderBy = pageableRequest.getOrderBy();
        String order = pageableRequest.getOrder();
        Integer page = pageableRequest.getPage();
        Integer size = pageableRequest.getSize();
        Sort sort = order.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(orderBy).ascending() : Sort.by(orderBy).descending();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<UserResponse> pageResult = usersService.findAll(username, email, isDeleted, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, orderBy, order));

    }

    /**
     * Obtiene un usuario por su id
     *
     * @param id Id del usuario
     * @return Usuario
     */
    @Operation(summary = "Obtiene un usuario dado un id", description = "Obtiene un usuario dado un id")
    @Parameters({
            @Parameter(name = "id", description = "id del usuario", example = "660e8400-e29b-41d4-a716-446655440000"),
    })
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
    @Operation(summary = "Eimina un usuario", description = "Elimina un usuario")
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
     * @param user   Usuario autenticado
     * @param page   Página
     * @param size   Tamaño de la página
     * @param sortBy Campo por el que ordenar
     * @param order  Dirección de la ordenación
     * @return Pedidos del usuario autenticado
     */

    @GetMapping("/me/orders")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageResponse<Order>> getOrdersByUsuario(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order
    ) {
        Sort sort = order.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.of(orderService.getOrdersByClientId(user.getId(), pageable), sortBy, order));
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
