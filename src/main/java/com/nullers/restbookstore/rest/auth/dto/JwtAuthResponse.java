package com.nullers.restbookstore.rest.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for JWT response
 *
 * @Author Binwei Wang
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthResponse {
    @Schema(description = "Token", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJNYW5vbG81NHNzIiwiaWF0IjoxNzAxNjQwOTY1LCJleHAiOjE3MDE3MjczNjUsImV4dHJhIjp7fX0.MHwsKPYMzU2FAxAKDpDGZe9TqEPFVMNHrwbyi0z5j_hy_xextKpHOxjnYzenjyqWspUoYM0aHebawjePtnLe-w")
    private String token;
}
