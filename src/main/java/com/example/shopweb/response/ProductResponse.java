package com.example.shopweb.response;

import com.example.shopweb.entity.ProductEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse extends BaseResponse{
    private String title;
    private float price;
    private String thumbnail;
    private String description;

    @JsonProperty("category_id")
    private UUID categoryId;

    @JsonProperty("message")
    private String message;

    public static ProductResponse fromProduct(ProductEntity productEntity){
        ProductResponse productResponse = ProductResponse.builder()
                .title(productEntity.getName())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .thumbnail(productEntity.getThumbnail())
                .categoryId(productEntity.getCategory().getId())
                .build();
        productResponse.setCreateDate(productEntity.getCreateDate());
        productResponse.setModifyDate(productEntity.getModifyDate());
        return productResponse;
    }
}
