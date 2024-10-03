package com.example.shopweb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto{

    @JsonProperty("full_name")
    private String fullName;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @JsonProperty("retype_password")
    private String retypePassword;

    @Size(message = "Phone number is not exceed 10 characters",min = 10, max = 10)
    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    private String address;

    @JsonProperty("facebook_account_id")
    private int facebookAccountId;

    @JsonProperty("google_account_id")
    private int googleAccountId;

    @JsonProperty("role_id")
    private UUID roleId;
}