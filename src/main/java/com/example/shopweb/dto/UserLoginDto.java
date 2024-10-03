package com.example.shopweb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;


@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto{
    @Size(message = "Phone number is not exceed 10 characters", max = 10)
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "Password is mandatory")
    @JsonProperty("role_id")
    private UUID roleId;
}
