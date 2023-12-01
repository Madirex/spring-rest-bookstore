package com.nullers.restbookstore.rest.shop.controllers;

import com.nullers.restbookstore.pagination.exceptions.PageNotValidException;
import com.nullers.restbookstore.pagination.models.PageResponse;
import com.nullers.restbookstore.pagination.util.PaginationLinksUtils;
import com.nullers.restbookstore.rest.shop.dto.CreateShopDto;
import com.nullers.restbookstore.rest.shop.dto.GetShopDto;
import com.nullers.restbookstore.rest.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.rest.shop.exceptions.ShopNotFoundException;
import com.nullers.restbookstore.rest.shop.exceptions.ShopNotValidUUIDException;
import com.nullers.restbookstore.rest.shop.services.ShopServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

/**
 * Implementación del controlador REST para la gestión de tiendas (Shops).
 * Provee endpoints para operaciones CRUD sobre tiendas.
 *
 * @author alexdor00
 */
@RestController
@RequestMapping("/api/shops")
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
    public ResponseEntity<PageResponse<GetShopDto>> getAllShops(@Valid @RequestParam(required = false) Optional<String> name,
                                                                @RequestParam(required = false) Optional<String> location,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @RequestParam(defaultValue = "id") String sortBy,
                                                                @RequestParam(defaultValue = "asc") String direction,
                                                                HttpServletRequest request
    ) {
        if (page < 0 || size < 1) {
            throw new PageNotValidException("La página no puede ser menor que 0 y su tamaño no debe de ser menor a 1.");
        }
        Sort sort = direction.equalsIgnoreCase(
                Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        var pageResult = shopService.getAllShops(name, location, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
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
    public ResponseEntity<GetShopDto> getShopById(@Valid @PathVariable String id) throws ShopNotFoundException {
        try {
            return ResponseEntity.ok(shopService.getShopById(id));
        } catch (ShopNotValidUUIDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // UUID no válido
        }
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
     * @param id      ID de la tienda a actualizar.
     * @param shopDto DTO con los datos actualizados de la tienda.
     * @return ResponseEntity con los detalles de la tienda actualizada en formato DTO.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     */
    @PutMapping("/{id}")
    @Override
    public ResponseEntity<GetShopDto> updateShop(@Valid @PathVariable String id, @Valid @RequestBody UpdateShopDto shopDto)
            throws ShopNotFoundException {
        try {
            return ResponseEntity.ok(shopService.updateShop(id, shopDto));
        } catch (ShopNotValidUUIDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // UUID no válido
        }
    }

    /**
     * Elimina una tienda específica por su ID.
     *
     * @param id ID de la tienda a eliminar.
     * @return ResponseEntity sin contenido indicando que la tienda ha sido eliminada.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     */
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<String> deleteShop(@Valid @PathVariable String id) throws ShopNotFoundException {
        try {
            shopService.deleteShop(id);
            return ResponseEntity.noContent().build();
        } catch (ShopNotValidUUIDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // UUID no válido
        }
    }
}
