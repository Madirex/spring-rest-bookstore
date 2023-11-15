package com.nullers.restbookstore.shop.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetShopDto {
    private UUID id;
    private String name;
    private String location;


    @Setter

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}