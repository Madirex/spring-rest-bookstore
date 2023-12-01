package com.nullers.restbookstore.rest.client.dto;

import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import lombok.*;

import java.util.ArrayList;
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
    private String image;

    @Builder.Default
    private List<GetBookDTO> books = new ArrayList<>();

}
