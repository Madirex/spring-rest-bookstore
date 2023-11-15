package com.nullers.restbookstore.shop.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PatchShopDto {
    private String name;
    private String location;
}
