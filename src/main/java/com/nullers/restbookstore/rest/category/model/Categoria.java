package com.nullers.restbookstore.rest.category.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
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
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nombre;

    private boolean activa;

    @Temporal(TemporalType.TIMESTAMP)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private LocalDateTime fecha_creacion;

    @Temporal(TemporalType.TIMESTAMP)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    @UpdateTimestamp
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

}