package com.example.shopweb.response;

import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.util.List;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class ProductListResponse {
    private List<ProductResponse> products;
    private int totalPages;
}
