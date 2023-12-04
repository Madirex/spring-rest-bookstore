package com.nullers.restbookstore.rest.user.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Entidad User
 *
 * @Author Binwei Wang
 */
@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bookstore_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(name = "ID", example = "660e8400-e29b-41d4-a716-446655440000")
    private UUID id;
    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false)
    @Schema(name = "Nombre", example = "Pedro")
    private String name;
    @NotBlank(message = "El apellido no puede estar vacío")
    @Column(nullable = false)
    @Schema(name = "Apellido", example = "Gómez")
    private String surname;
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Column(nullable = false, unique = true)
    @Schema(name = "Nombre de usuario", example = "usuario1")
    private String username;
    @Email(regexp = ".*@.*\\..*", message = "El email debe ser válido")
    @NotBlank(message = "El email no puede estar vacío")
    @Schema(name = "Email", example = "ejemplo@gmail.com")
    private String email;
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 5, message = "La contraseña debe tener al menos 5 caracteres")
    @Column(nullable = false)
    @Schema(name = "Contraseña", example = "ejemplo1234")
    private String password;
    @CreationTimestamp
    @Builder.Default
    @Schema(name = "Fecha de creación", example = "2021-03-05T11:11:11")
    private LocalDateTime createdAt = LocalDateTime.now();
    @UpdateTimestamp
    @Builder.Default
    @Schema(name = "Fecha de actualización", example = "2021-03-05T11:11:11")
    private LocalDateTime updatedAt = LocalDateTime.now();
    @Builder.Default
    @Schema(name = "Usuario borrado", example = "true")
    private Boolean isDeleted = false;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Schema(name = "Roles")
    private Set<Role> roles;

    /**
     * Retorna los roles del usuario
     *
     * @return roles del usuario
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }

    /**
     * Retorna la clave primaria (username)
     *
     * @return username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Comprueba si la cuenta del usuario ha expirado
     *
     * @return true si no ha expirado, false si lo ha hecho
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Comprueba si la cuenta del usuario está bloqueada
     *
     * @return true si no está bloqueada, false si lo está
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Comprueba si las credenciales del usuario han expirado
     *
     * @return true si no han expirado, false si lo han hecho
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Comprueba si el usuario está borrado
     *
     * @return true si no está borrado, false si lo está
     */
    @Override
    public boolean isEnabled() {
        return !isDeleted;
    }
}
