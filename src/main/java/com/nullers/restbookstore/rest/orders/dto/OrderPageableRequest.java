package com.nullers.restbookstore.rest.orders.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPageableRequest {

    @Min(value = 0, message = "La página no puede ser inferior a 0")
    @Builder.Default()
    private Integer page = 0;

    @Min(value = 1, message = "El tamaño de la pagina no puede ser inferior a 1")
    @Builder.Default()
    private Integer size = 10;

    @Builder.Default()
    private String orderBy = "id";

    @Builder.Default()
    private String order = "ASC";

}
