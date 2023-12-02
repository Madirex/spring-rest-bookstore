package com.nullers.restbookstore.rest.client.dto;

import com.nullers.restbookstore.rest.common.Address;
import lombok.*;

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
    private Address address;
    private String image;

}
