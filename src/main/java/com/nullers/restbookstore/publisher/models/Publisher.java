package com.nullers.restbookstore.publisher.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "publishers")
public class Publisher {
    public static final String DEFAULT_IMAGE = "https://via.placeholder.com/200";
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT default '" + DEFAULT_IMAGE + "'")
    @Builder.Default
    private String image = DEFAULT_IMAGE;

    @OneToMany
    @JoinColumn(name = "book_id")
    @Builder.Default
    private Set<Book> books = new HashSet<>();

    @CreatedDate
    private LocalDateTime created_at;

    @LastModifiedDate
    private LocalDateTime updated_at;
}
