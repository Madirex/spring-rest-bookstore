package com.nullers.restbookstore.rest.orders.controllers;

import com.nullers.restbookstore.pagination.models.ErrorResponse;
import com.nullers.restbookstore.pagination.models.PageResponse;
import com.nullers.restbookstore.pagination.util.PaginationLinksUtils;
import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.client.exceptions.ClientNotFound;
import com.nullers.restbookstore.rest.common.PageableRequest;
import com.nullers.restbookstore.rest.orders.dto.OrderCreateDto;
import com.nullers.restbookstore.rest.orders.exceptions.OrderBadPriceException;
import com.nullers.restbookstore.rest.orders.exceptions.OrderNotFoundException;
import com.nullers.restbookstore.rest.orders.exceptions.OrderNotItemsExceptions;
import com.nullers.restbookstore.rest.orders.exceptions.OrderNotStockException;
import com.nullers.restbookstore.rest.orders.models.Order;
import com.nullers.restbookstore.rest.orders.services.OrderServiceImpl;
import com.nullers.restbookstore.rest.shop.exceptions.ShopNotFoundException;
import com.nullers.restbookstore.rest.user.exceptions.UserNotFound;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@RestController
@RequestMapping("/api/orders")
@PreAuthorize("hasRole('ADMIN')")
public class OrderRestController {

    private final OrderServiceImpl orderService;

    private final PaginationLinksUtils paginationLinksUtils;

    @Autowired
    public OrderRestController(OrderServiceImpl orderService, PaginationLinksUtils paginationLinksUtils) {
        this.orderService = orderService;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    @Operation(summary = "Obtiene todos los pedidos", description = "Obtiene una lista de pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de pedidos"),
            @ApiResponse(responseCode = "400", description = "Petición de pedidos no válida")
    })
    @GetMapping
    public ResponseEntity<PageResponse<Order>> getAllOrders(
            @Valid PageableRequest pageableRequest,
            HttpServletRequest request
    ) {
        String orderBy = pageableRequest.getOrderBy();
        String order = pageableRequest.getOrder();
        Integer page = pageableRequest.getPage();
        Integer size = pageableRequest.getSize();
        Sort sort = order.equalsIgnoreCase("ASC") ? Sort.by(orderBy).ascending() : Sort.by(orderBy).descending();
        Page<Order> orders = orderService.getAllOrders(PageRequest.of(page, size, sort));
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(orders, uriBuilder))
                .body(PageResponse.of(orders, orderBy, order));
    }

    @Operation(summary = "Obtiene un pedido dado un id", description = "Obtiene un pedido dado un id")
    @Parameters({
            @Parameter(name = "id", description = "id del pedido", example = "770e8400-e29b-41d4-a716-446655440000"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable ObjectId id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @Operation(summary = "Crea un pedido", description = "Crea un pedido")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Pedido a crear", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido creado"),
            @ApiResponse(responseCode = "400", description = "Pedido no válido")
    })
    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderCreateDto order) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(order));
    }

    @Operation(summary = "Actualiza un pedido", description = "Actualiza un pedido")
    @Parameter(name = "id", description = "id del pedido a actualizar", example = "770e8400-e29b-41d4-a716-446655440000")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Pedido actualizado", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido actualizado"),
            @ApiResponse(responseCode = "400", description = "Pedido no válido"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable ObjectId id, @Valid @RequestBody OrderCreateDto order) {
        return ResponseEntity.ok(orderService.updateOrder(id, order));
    }

    @Operation(summary = "Eimina un pedido", description = "Elimina un pedido")
    @Parameter(name = "id", description = "Id del pedido a eliminar", example = "660e8400-e29b-41d4-a716-446655440000", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido borrado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable ObjectId id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eimina un pedido de manera simulada", description = "Elimina un pedido de manera simulada")
    @Parameter(name = "id", description = "Id del pedido a eliminar", example = "660e8400-e29b-41d4-a716-446655440000", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido borrado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
    })
    @PutMapping("/delete/{id}")
    public ResponseEntity<Order> deleteLogicOrder(@PathVariable ObjectId id) {
        return ResponseEntity.ok(orderService.deleteLogicOrder(id));
    }

    @Operation(summary = "Obtiene un pedido dado el id de un cliente", description = "Obtiene un pedido dado el id de un cliente")
    @Parameters({
            @Parameter(name = "id", description = "id del cliente", example = "770e8400-e29b-41d4-a716-446655440000"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/client/{id}")
    public ResponseEntity<PageResponse<Order>> getOrdersByClientId(
            @PathVariable UUID id,
            @Valid PageableRequest pageableRequest,
            HttpServletRequest request
    ) {
        String orderBy = pageableRequest.getOrderBy();
        String order = pageableRequest.getOrder();
        Integer page = pageableRequest.getPage();
        Integer size = pageableRequest.getSize();
        Sort sort = order.equalsIgnoreCase("ASC") ? Sort.by(orderBy).ascending() : Sort.by(orderBy).descending();
        Page<Order> orders = orderService.getOrdersByClientId(id, PageRequest.of(page, size, sort));
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(orders, uriBuilder))
                .body(PageResponse.of(orders, orderBy, order));
    }

    @Operation(summary = "Obtiene un pedido dado el id de un usuario", description = "Obtiene un pedido dado el id de un usuario")
    @Parameters({
            @Parameter(name = "id", description = "id del usuario", example = "770e8400-e29b-41d4-a716-446655440000"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
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
        Sort sort = order.equalsIgnoreCase("ASC") ? Sort.by(orderBy).ascending() : Sort.by(orderBy).descending();
        Page<Order> orders = orderService.getOrdersByUserId(id, PageRequest.of(page, size, sort));
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(orders, uriBuilder))
                .body(PageResponse.of(orders, orderBy, order));
    }

    @Operation(summary = "Obtiene un pedido dado el id de una tienda", description = "Obtiene un pedido dado el id de una tienda")
    @Parameters({
            @Parameter(name = "id", description = "id de la tienda", example = "770e8400-e29b-41d4-a716-446655440000"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido"),
            @ApiResponse(responseCode = "404", description = "Tienda no encontrada")
    })
    @GetMapping("/shop/{id}")
    public ResponseEntity<PageResponse<Order>> getOrdersByShopId(
            @PathVariable UUID id,
            @Valid PageableRequest pageableRequest,
            HttpServletRequest request
    ) {
        String orderBy = pageableRequest.getOrderBy();
        String order = pageableRequest.getOrder();
        Integer page = pageableRequest.getPage();
        Integer size = pageableRequest.getSize();
        Sort sort = order.equalsIgnoreCase("ASC") ? Sort.by(orderBy).ascending() : Sort.by(orderBy).descending();
        Page<Order> orders = orderService.getOrdersByShopId(id, PageRequest.of(page, size, sort));
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(orders, uriBuilder))
                .body(PageResponse.of(orders, orderBy, order));
    }


    @ExceptionHandler(UserNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFound e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(ClientNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleClientNotFound(ClientNotFound e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleOrderNotFoundException(OrderNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(OrderNotItemsExceptions.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleOrderNotItemsExceptions(OrderNotItemsExceptions e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(OrderBadPriceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleOrderBadPriceException(OrderBadPriceException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleBookNotFoundException(BookNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(OrderNotStockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleOrderNotStockException(OrderNotStockException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(ShopNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleShopNotFoundException(ShopNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }
}
