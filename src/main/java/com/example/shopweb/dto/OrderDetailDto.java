package com.example.shopweb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.UUID;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto{

    @Min(value = 0, message = "Price must be >= 0")
    private float price;

    @JsonProperty("number_of_products")
    @Min(value = 1,message = "Number Of Products must be >= 1")
    private int numberOfProducts;

    @JsonProperty("total_money")
    @Min(value = 0,message = "Total money must be >= 0")
    private float totalMoney;

    private String color;

    @JsonProperty("order_id")
    private UUID orderId;

    @JsonProperty("product_id")
    private UUID productId;
}