package com.nullers.restbookstore.rest.orders.dto;

import com.nullers.restbookstore.rest.orders.models.OrderLine;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreateDto {

    @NotNull(message = "El id del usuario no puede ser nulo")
    private UUID userId;

    @NotNull(message = "El cliente no puede ser nulo")
    private UUID clientId;

    @NotNull(message = "El pedido debe tener al menos una línea de pedido")
    private List<@Valid OrderLine> orderLines;

}
