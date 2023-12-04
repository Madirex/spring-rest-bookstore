package com.nullers.restbookstore.rest.orders.dto;

import com.nullers.restbookstore.rest.orders.models.OrderLine;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Clase OrderCreateDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreateDto {

    @NotNull(message = "El id del usuario no puede ser nulo")
    @Schema(name = "IDUsuario", example = "770e8400-e29b-41d4-a716-446655440000")
    private UUID userId;

    @NotNull(message = "El cliente no puede ser nulo")
    @Schema(name = "IDCliente", example = "770e8400-e29b-41d4-a716-446655440000")
    private UUID clientId;

    @NotNull(message = "La tienda no puede ser nula")
    @Schema(name = "IDTienda", example = "770e8400-e29b-41d4-a716-446655440000")
    private UUID shopId;

    @NotNull(message = "El pedido debe tener al menos una línea de pedido")
    @Schema(name = "Línea de pedidos")
    private List<@Valid OrderLine> orderLines;

}
