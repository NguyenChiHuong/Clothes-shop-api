package com.example.shopweb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;


@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto{

    private String username;

    @Email
    private String email;

    @NotBlank(message = "Phone number is mandatory")
    @Length(max = 10,min = 10,message = "Phone number must be equal 10 characters")
    @JsonProperty("phone_number")
    private String phoneNumber;

    private String address;

    private String note;

    @JsonProperty("total_money")
    @Min(value = 0, message = "Total Money >= 0")
    private Float totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("user_id")
    private UUID userId;
}