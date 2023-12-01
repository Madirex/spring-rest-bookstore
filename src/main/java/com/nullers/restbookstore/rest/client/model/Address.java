package com.nullers.restbookstore.rest.client.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record Address(
        @Length(min = 3,max = 200,message = "La calle debe tener al menos 3 caracteres y máximo 200")
        @NotBlank(message = "La calle no puede estar vacía")
        String street,

        @NotBlank(message = "El número no puede estar vacío")
        @Length(min = 0,message = "El número debe tener al menos 1 caracter")
        String number,

        @Length(min = 3, message = "La ciudad debe tener al menos 3 caracteres")
        @NotBlank(message = "La ciudad no puede estar vacía")
        String city,

        @Length(min = 3, message = "La provincia debe tener al menos 3 caracteres")
        String province,

        @Length(min = 3, message = "El país debe tener al menos 3 caracteres")
        @NotBlank(message = "El país no puede estar vacío")
        String country,

        @NotBlank(message = "El código postal no puede estar vacío")
        @Pattern(regexp = "^[0-9]{5}$", message = "El código postal debe tener 5 dígitos")
        String PostalCode
) {}