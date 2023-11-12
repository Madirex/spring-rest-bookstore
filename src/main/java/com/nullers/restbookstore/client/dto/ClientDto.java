package com.nullers.restbookstore.client.dto;

import com.nullers.restbookstore.NOADD.models.Book;
import lombok.*;

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
    private List<Book> books;

}
