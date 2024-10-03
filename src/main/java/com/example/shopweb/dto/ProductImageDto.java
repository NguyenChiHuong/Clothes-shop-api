package com.example.shopweb.dto;

import lombok.*;

import java.util.UUID;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageDto{

    private String imageUrl;

    private UUID productId;
}
