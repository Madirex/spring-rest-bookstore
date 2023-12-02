package com.nullers.restbookstore.rest.user.models;

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
    private UUID id;
    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false)
    private String name;
    @NotBlank(message = "El apellido no puede estar vacío")
    @Column(nullable = false)
    private String surnames;
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Column(nullable = false, unique = true)
    private String username;
    @Email(regexp = ".*@.*\\..*", message = "El email debe ser válido")
    @NotBlank(message = "El email no puede estar vacío")
    private String email;
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 5, message = "La contraseña debe tener al menos 5 caracteres")
    @Column(nullable = false)
    private String password;
    @CreationTimestamp
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @UpdateTimestamp
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    @Builder.Default
    private Boolean isDeleted = false;
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getUsername(){
        return username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !isDeleted;
    }
}
