package com.nullers.restbookstore.rest.shop.controllers;

import com.nullers.restbookstore.pagination.models.PageResponse;
import com.nullers.restbookstore.pagination.util.PaginationLinksUtils;
import com.nullers.restbookstore.rest.common.PageableRequest;
import com.nullers.restbookstore.rest.common.PageableUtil;
import com.nullers.restbookstore.rest.shop.dto.CreateShopDto;
import com.nullers.restbookstore.rest.shop.dto.GetShopDto;
import com.nullers.restbookstore.rest.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.rest.shop.exceptions.ShopNotFoundException;
import com.nullers.restbookstore.rest.shop.services.ShopServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementación del controlador REST para la gestión de tiendas (Shops).
 * Provee endpoints para operaciones CRUD sobre tiendas.
 *
 * @author alexdor00
 */
@RestController
@RequestMapping("/api/shops")
@PreAuthorize("hasRole('ADMIN')")
public class ShopRestControllerImpl implements ShopRestController {

    private final ShopServiceImpl shopService;
    private final PaginationLinksUtils paginationLinksUtils;

    /**
     * Constructor que inyecta el servicio de Shop.
     *
     * @param shopService          Servicio de Shop para operaciones de negocio.
     * @param paginationLinksUtils Utilidades para la paginación.
     */
    @Autowired
    public ShopRestControllerImpl(ShopServiceImpl shopService, PaginationLinksUtils paginationLinksUtils) {
        this.shopService = shopService;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    /**
     * Obtiene todas las tiendas disponibles.
     *
     * @return ResponseEntity con una lista de todas las tiendas en formato DTO.
     */
    @Operation(summary = "Obtiene todas las tiendas", description = "Obtiene una lista de todas las tiendas disponibles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tiendas obtenida con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    @GetMapping
    public ResponseEntity<PageResponse<GetShopDto>> getAllShops(
            @Parameter(description = "Nombre de la tienda para filtrar") @Valid @RequestParam(required = false) Optional<String> name,
            @Parameter(description = "Ubicación de la tienda para filtrar") @RequestParam(required = false) Optional<String> location,
            @Parameter(description = "Parámetros de paginación") @Valid PageableRequest pageableRequest,
            HttpServletRequest request
    ) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        var pageResult = shopService.getAllShops(name, location, PageRequest.of(pageableRequest.getPage(),
                pageableRequest.getSize(), PageableUtil.getSort(pageableRequest)));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, pageableRequest.getOrderBy(), pageableRequest.getOrder()));
    }

    /**
     * Obtiene una tienda específica por su ID.
     *
     * @param id ID de la tienda en formato String.
     * @return ResponseEntity con los detalles de la tienda en formato DTO.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     */
    @Operation(summary = "Obtiene una tienda por su ID", description = "Obtiene los detalles de una tienda específica por su ID.")
    @Parameter(name = "id", description = "Identificador de la tienda", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalles de la tienda obtenidos con éxito"),
            @ApiResponse(responseCode = "404", description = "Tienda no encontrada")
    })
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<GetShopDto> getShopById(@Valid @PathVariable UUID id) throws ShopNotFoundException {
        return ResponseEntity.ok(shopService.getShopById(id));
    }

    /**
     * Crea una nueva tienda con los datos proporcionados.
     *
     * @param shopDto DTO con la información de la tienda a crear.
     * @return ResponseEntity con los detalles de la tienda creada en formato DTO.
     */
    @Operation(summary = "Crea una nueva tienda", description = "Crea una tienda con la información proporcionada.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la nueva tienda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tienda creada con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos de la tienda no válidos")
    })
    @PostMapping
    @Override
    public ResponseEntity<GetShopDto> createShop(@Valid @RequestBody CreateShopDto shopDto) {
        GetShopDto newShop = shopService.createShop(shopDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newShop);
    }

    /**
     * Actualiza una tienda existente con los datos proporcionados.
     *
     * @param id      Id de la tienda a actualizar.
     * @param shopDto DTO con los datos actualizados de la tienda.
     * @return ResponseEntity con los detalles de la tienda actualizada en formato DTO.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     */
    @Operation(summary = "Actualiza una tienda existente", description = "Actualiza una tienda con los datos proporcionados.")
    @Parameter(name = "id", description = "Identificador de la tienda a actualizar", required = true)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados de la tienda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tienda actualizada con éxito"),
            @ApiResponse(responseCode = "404", description = "Tienda no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos de la tienda no válidos")
    })
    @PutMapping("/{id}")
    @Override
    public ResponseEntity<GetShopDto> updateShop(@Valid @PathVariable UUID id, @Valid @RequestBody UpdateShopDto shopDto) {
        return ResponseEntity.ok(shopService.updateShop(id, shopDto));
    }

    /**
     * Elimina una tienda específica por su ID.
     *
     * @param id Id de la tienda a eliminar.
     * @return ResponseEntity sin contenido indicando que la tienda ha sido eliminada.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     */
    @Operation(summary = "Elimina una tienda por su ID", description = "Elimina una tienda específica por su ID.")
    @Parameter(name = "id", description = "Identificador de la tienda a eliminar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tienda eliminada con éxito"),
            @ApiResponse(responseCode = "404", description = "Tienda no encontrada")
    })
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<String> deleteShop(@PathVariable UUID id) {
        shopService.deleteShop(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Añade un libro a una tienda específica.
     *
     * @param id      ID de la tienda.
     * @param bookId  ID del libro a añadir.
     * @return ResponseEntity con los detalles de la tienda actualizada.
     */
    @Operation(summary = "Añade un libro a una tienda", description = "Añade un libro a una tienda específica por su ID.")
    @Parameter(name = "id", description = "Identificador de la tienda", required = true)
    @Parameter(name = "bookId", description = "Identificador del libro a añadir", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro añadido a la tienda con éxito"),
            @ApiResponse(responseCode = "404", description = "Tienda o libro no encontrado")
    })
    @PatchMapping("/{id}/books/{bookId}")
    public ResponseEntity<GetShopDto> addBookToShop(@Valid @PathVariable UUID id, @Valid @PathVariable Long bookId) {
        return ResponseEntity.ok(shopService.addBookToShop(id, bookId));
    }

    /**
     * Elimina un libro de una tienda específica.
     *
     * @param id      ID de la tienda.
     * @param bookId  ID del libro a eliminar.
     * @return ResponseEntity con los detalles de la tienda actualizada.
     */
    @Operation(summary = "Elimina un libro de una tienda", description = "Elimina un libro de una tienda específica por su ID.")
    @Parameter(name = "id", description = "Identificador de la tienda", required = true)
    @Parameter(name = "bookId", description = "Identificador del libro a eliminar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro eliminado de la tienda con éxito"),
            @ApiResponse(responseCode = "404", description = "Tienda o libro no encontrado")
    })
    @Override
    @DeleteMapping("/{id}/books/{bookId}")
    public ResponseEntity<GetShopDto> removeBookFromShop(@Valid @PathVariable UUID id, @Valid @PathVariable Long bookId) {
        return ResponseEntity.ok(shopService.removeBookFromShop(id, bookId));
    }

    /**
     * Añade un cliente a una tienda específica.
     *
     * @param id        ID de la tienda.
     * @param clientId  ID del cliente a añadir.
     * @return ResponseEntity con los detalles de la tienda actualizada.
     */
    @Operation(summary = "Añade un cliente a una tienda", description = "Añade un cliente a una tienda específica por su ID.")
    @Parameter(name = "id", description = "Identificador de la tienda", required = true)
    @Parameter(name = "clientId", description = "Identificador del cliente a añadir", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente añadido a la tienda con éxito"),
            @ApiResponse(responseCode = "404", description = "Tienda o cliente no encontrado")
    })
    @Override
    @PatchMapping("/{id}/clients/{clientId}")
    public ResponseEntity<GetShopDto> addClientToShop(@Valid @PathVariable UUID id, @Valid @PathVariable UUID clientId) {
        return ResponseEntity.ok(shopService.addClientToShop(id, clientId));
    }

    /**
     * Elimina un cliente de una tienda específica.
     *
     * @param id        ID de la tienda.
     * @param clientId  ID del cliente a eliminar.
     * @return ResponseEntity con los detalles de la tienda actualizada.
     */
    @Operation(summary = "Elimina un cliente de una tienda", description = "Elimina un cliente de una tienda específica por su ID.")
    @Parameter(name = "id", description = "Identificador de la tienda", required = true)
    @Parameter(name = "clientId", description = "Identificador del cliente a eliminar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente eliminado de la tienda con éxito"),
            @ApiResponse(responseCode = "404", description = "Tienda o cliente no encontrado")
    })
    @Override
    @DeleteMapping("/{id}/clients/{clientId}")
    public ResponseEntity<GetShopDto> removeClientFromShop(@Valid @PathVariable UUID id, @Valid @PathVariable UUID clientId) {
        return ResponseEntity.ok(shopService.removeClientFromShop(id, clientId));
    }


    @ExceptionHandler(ClientNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleClientNotFound(ClientNotFound ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(ShopNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleShopNotFound(ShopNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleBookNotFound(BookNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(ShopHasOrders.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleShopHasOrders(ShopHasOrders ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }
}



