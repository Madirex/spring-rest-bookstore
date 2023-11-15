package com.nullers.restbookstore.rest.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Data
@Builder
public class CategoriaCreateDto {

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @Builder.Default
    private boolean activa = true;


}
