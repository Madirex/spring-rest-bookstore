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
    @GetMapping
    public ResponseEntity<PageResponse<GetShopDto>> getAllShops(
            @Valid @RequestParam(required = false) Optional<String> name,
            @RequestParam(required = false) Optional<String> location,
            @Valid PageableRequest pageableRequest,
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
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<String> deleteShop(@PathVariable UUID id) {
        shopService.deleteShop(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * Añade un libro a una tienda
     *
     * @param id     Identificador de la tienda
     * @param bookId Identificador del libro
     * @return ResponseEntity con la tienda actualizada en formato DTO.
     */
    @PatchMapping("/{id}/books/{bookId}")
    public ResponseEntity<GetShopDto> addBookToShop(@Valid @PathVariable UUID id, @Valid @PathVariable Long bookId) {
        return ResponseEntity.ok(shopService.addBookToShop(id, bookId));
    }

    /**
     * Elimina un libro de una tienda
     *
     * @param id     Identificador de la tienda
     * @param bookId Identificador del libro
     * @return ResponseEntity con la tienda actualizada en formato DTO.
     */
    @Override
    @DeleteMapping("/{id}/books/{bookId}")
    public ResponseEntity<GetShopDto> removeBookFromShop(@Valid @PathVariable UUID id, @Valid @PathVariable Long bookId) {
        return ResponseEntity.ok(shopService.removeBookFromShop(id, bookId));
    }

    /**
     * Añade un cliente a una tienda
     *
     * @param id       Identificador de la tienda
     * @param clientId Identificador del cliente
     * @return ResponseEntity con la tienda actualizada en formato DTO.
     */
    @Override
    @PatchMapping("/{id}/clients/{clientId}")
    public ResponseEntity<GetShopDto> addClientToShop(@Valid @PathVariable UUID id, @Valid @PathVariable UUID clientId) {
        return ResponseEntity.ok(shopService.addClientToShop(id, clientId));
    }

    /**
     * Elimina un cliente de una tienda
     *
     * @param id       Identificador de la tienda
     * @param clientId Identificador del cliente
     * @return ResponseEntity con la tienda actualizada en formato DTO.
     */
    @Override
    @DeleteMapping("/{id}/clients/{clientId}")
    public ResponseEntity<GetShopDto> removeClientFromShop(@Valid @PathVariable UUID id, @Valid @PathVariable UUID clientId) {
        return ResponseEntity.ok(shopService.removeClientFromShop(id, clientId));
    }
}
