package com.example.shopweb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto{
    @NotNull
    @NotEmpty
    @NotBlank
    private String token;

    @NotNull
    private String tokenType;

    @NotNull
    private LocalDateTime expirationDate;

    private int revoked;
    private int expired;
}