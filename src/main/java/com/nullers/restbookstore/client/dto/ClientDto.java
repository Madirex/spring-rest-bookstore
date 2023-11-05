package com.nullers.restbookstore.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

    private UUID id;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String address;
    private String qrImage;
    private List<UUID> booksId;

}
