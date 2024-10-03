package com.example.shopweb.response;

import com.example.shopweb.entity.OrderDetailEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse extends BaseResponse{
    private float price;

    @JsonProperty("number_of_products")
    private int numberOfProducts;

    @JsonProperty("total_money")
    private float totalMoney;

    private String color;

    @JsonProperty("order_id")
    private UUID orderId;

    @JsonProperty("product_id")
    private UUID productId;

    @JsonProperty("message")
    private String message;

    public static OrderDetailResponse fromOrderDetail(OrderDetailEntity orderDetailEntity){
        OrderDetailResponse orderDetailResponse = OrderDetailResponse.builder()
                .price(orderDetailEntity.getPrice())
                .numberOfProducts(orderDetailEntity.getNumberOfProducts())
                .totalMoney(orderDetailEntity.getTotalMoney())
                .color(orderDetailEntity.getColor())
                .orderId(orderDetailEntity.getOrder().getId())
                .productId(orderDetailEntity.getProduct().getId())
                .build();
        orderDetailResponse.setId(orderDetailEntity.getId());
        orderDetailResponse.setCreateDate(orderDetailEntity.getCreateDate());
        orderDetailResponse.setModifyDate(orderDetailEntity.getModifyDate());
        return orderDetailResponse;
    }
}
