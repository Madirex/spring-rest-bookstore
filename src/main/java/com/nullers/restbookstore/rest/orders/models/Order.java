package com.nullers.restbookstore.rest.orders.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Clase Order
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("orders")
@TypeAlias("Order")
public class Order {

    @Id
    @Builder.Default
    private ObjectId id = new ObjectId();

    @NotNull(message = "El id del usuario no puede ser nulo")
    @Schema(name = "IDUsuario", example = "770e8400-e29b-41d4-a716-446655440000")
    private UUID userId;

    @NotNull(message = "El cliente no puede ser nulo")
    @Schema(name = "IDCliente", example = "770e8400-e29b-41d4-a716-446655440000")
    private UUID clientId;

    @NotNull(message = "El id de la tienda no puede ser nulo")
    @Schema(name = "IDTienda", example = "770e8400-e29b-41d4-a716-446655440000")
    private UUID shopId;

    @NotNull(message = "El pedido debe tener al menos una línea de pedido")
    @Schema(name = "Línea de pedidos")
    private List<@Valid OrderLine> orderLines;

    @Builder.Default
    @Schema(name = "Total", example = "100.0")
    private Double total = 0.0;

    @Builder.Default
    @Schema(name = "Libros totales", example = "10")
    private Integer totalBooks = 0;

    @Builder.Default
    @CreationTimestamp
    @Schema(name = "Fecha de creación", example = "2021-03-05T11:11:11")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @UpdateTimestamp
    @Schema(name = "Fecha de actualización", example = "2021-03-05T11:11:11")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder.Default
    @Schema(name = "Usuario borrado", example = "true")
    private Boolean isDeleted = false;

    /**
     * Método que devuelve el ID del pedido
     *
     * @return id del pedido
     */
    @JsonProperty("id")
    public String getIdStr() {
        return id.toHexString();
    }

    /**
     * Método que calcula el total y el total de libros del pedido
     */
    public void calculateLines() {
        this.totalBooks = orderLines != null ? orderLines.size() : 0;
        this.total = orderLines != null ? orderLines.stream().mapToDouble(OrderLine::getTotal).sum() : 0.0;
    }

}
