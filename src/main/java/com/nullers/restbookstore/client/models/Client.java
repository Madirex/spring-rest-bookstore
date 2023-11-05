package com.nullers.restbookstore.client.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String address;

    @OneToMany
    @JoinColumn(name = "book_id")
    private List<Book> books;
    private LocalDateTime createdAt;
    private String qrImage;
}
