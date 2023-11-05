package com.nullers.restbookstore.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientCreateDto {

    private String name;
    private String surname;
    private String email;
    private String phone;
    private String address;
    private String qrImage;

}
