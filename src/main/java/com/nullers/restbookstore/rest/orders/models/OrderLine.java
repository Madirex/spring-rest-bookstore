package com.nullers.restbookstore.rest.orders.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderLine {

    @Min(value = 1, message = "La cantidad del libro no puede ser negativa")
    @Builder.Default
    @Schema(name = "Cantidad", example = "1")
    private Integer quantity = 1;

    @NotNull(message = "El id del libro no puede ser nulo")
    @Schema(name = "IDLibro", example = "1")
    private Long bookId;

    @Min(value = 0,message = "El precio del libro no puede ser negativo")
    @Builder.Default
    @Schema(name = "Precio", example = "10.0")
    private Double price = 0.0;

    @Builder.Default
    @Schema(name = "Total", example = "100.0")
    private Double total = 0.0;

    public void setQuantity(Integer qty) {
        this.quantity = qty;
        this.total = this.price * this.quantity;
    }

    public void calculatePrice(Double price){
        this.price = price;
        this.total = this.price * this.quantity;
    }

    public void setTotal(Double total){
        this.total = total;
    }

}
