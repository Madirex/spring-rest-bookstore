package com.nullers.restbookstore.rest.orders.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase OrderLine
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderLine {

    @Min(value = 1, message = "La cantidad del libro no puede ser negativa")
    @Builder.Default
    private Integer quantity = 1;

    @NotNull(message = "El id del libro no puede ser nulo")
    private Long bookId;

    @Min(value = 0, message = "El precio del libro no puede ser negativo")
    @Builder.Default
    private Double price = 0.0;

    @Builder.Default
    private Double total = 0.0;

    /**
     * Set de la cantidad
     *
     * @param qty Cantidad
     */
    public void setQuantity(Integer qty) {
        this.quantity = qty;
        this.total = this.price * this.quantity;
    }

    /**
     * Calcula el precio de la línea de pedido
     *
     * @param price Precio del libro
     */
    public void calculatePrice(Double price) {
        this.price = price;
        this.total = this.price * this.quantity;
    }

    /**
     * Set del Total
     *
     * @param total Total
     */
    public void setTotal(Double total) {
        this.total = total;
    }

}
