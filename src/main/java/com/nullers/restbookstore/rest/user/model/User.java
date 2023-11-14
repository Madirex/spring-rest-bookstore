package com.nullers.restbookstore.rest.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * entidad User
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USUARIOS")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false)
    private String nombre;
    @NotBlank(message = "El apellido no puede estar vacío")
    @Column(nullable = false)
    private String apellidos;
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Column(nullable = false, unique = true)
    private String username;
    @Email(regexp = ".*@.*\\..*", message = "El email debe ser válido")
    @NotBlank(message = "El email no puede estar vacío")
    private String email;
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Min(value = 5, message = "La contraseña debe tener al menos 5 caracteres")
    @Column(nullable = false)
    private String password;
    @CreationTimestamp
    @Builder.Default
    private LocalDateTime createAt = LocalDateTime.now();
    @UpdateTimestamp
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    @Builder.Default
    private Boolean isDeleted = false;

    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
}
