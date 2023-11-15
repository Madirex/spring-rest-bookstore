package com.nullers.restbookstore.shop.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * Clase PatchShopDto
 *
 *  @author alexdor00
 */

@Getter
@Builder
public class PatchShopDto {
    private String name;
    private String location;
}
