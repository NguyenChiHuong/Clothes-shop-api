package com.example.shopweb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SocialAccountDto{
    @NotNull
    @NotEmpty
    private String provider;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String name;
}