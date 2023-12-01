package com.nullers.restbookstore.rest.orders.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nullers.restbookstore.rest.client.model.Client;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("orders")
@TypeAlias("Order")
public class Order {

    @Id
    @Builder.Default
    private ObjectId id = new ObjectId();

    @NotNull(message = "El id del usuario no puede ser nulo")
    private UUID userId;

    @NotNull(message = "El cliente no puede ser nulo")
    private UUID clientId;

    @NotNull(message = "El pedido debe tener al menos una línea de pedido")
    private List<@Valid OrderLine> orderLines;

    @Builder.Default
    private Double total = 0.0;

    @Builder.Default
    private Integer totalBooks = 0;

    @Builder.Default
    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @UpdateTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder.Default
    private boolean isDeleted = false;

    @JsonProperty("id")
    public String get_id() {
        return id.toHexString();
    }

    public void calculateLines(){
        this.totalBooks = orderLines != null ? orderLines.size() : 0;
        this.total = orderLines != null ? orderLines.stream().mapToDouble(OrderLine::getTotal).sum() : 0.0;
    }

}
