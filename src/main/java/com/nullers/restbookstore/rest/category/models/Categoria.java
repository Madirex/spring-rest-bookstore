package com.nullers.restbookstore.rest.category.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DialectOverride;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@Table(name = "categorias")
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id = UUID.randomUUID();

    @Column(nullable = false, unique = true)
    private String nombre;

    private boolean activa;

    @Temporal(TemporalType.TIMESTAMP)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private LocalDateTime fecha_creacion;

    @Temporal(TemporalType.TIMESTAMP)
    @ColumnDefault(value = "CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime fecha_actualizacion;

    @PrePersist
    protected void onCreate() {
        if (fecha_creacion == null) {
            fecha_creacion = LocalDateTime.now();
        }
        if (fecha_actualizacion == null) {
            fecha_actualizacion = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fecha_actualizacion = LocalDateTime.now();
    }
}