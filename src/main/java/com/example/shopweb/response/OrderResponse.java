package com.example.shopweb.response;

import com.example.shopweb.entity.OrderEntity;
import com.example.shopweb.entity.ProductEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse extends BaseResponse{

    private String username;
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String address;

    private String note;

    private String status;

    @JsonProperty("order_date")
    private Date orderDate;

    @JsonProperty("total_money")
    private Float totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("tracking_number")
    private String trackingNumber;

    @JsonProperty("active")
    private Boolean active;

    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("message")
    private String message;

    public static OrderResponse fromOrder(OrderEntity orderEntity){
        OrderResponse orderResponse = OrderResponse.builder()
                .username(orderEntity.getUsername())
                .email(orderEntity.getEmail())
                .phoneNumber(orderEntity.getPhoneNumber())
                .address(orderEntity.getAddress())
                .note(orderEntity.getNote())
                .orderDate(orderEntity.getOrderDate())
                .totalMoney(orderEntity.getTotalMoney())
                .shippingMethod(orderEntity.getShippingMethod())
                .shippingAddress(orderEntity.getShippingAddress())
                .shippingDate(orderEntity.getShippingDate())
                .paymentMethod(orderEntity.getPaymentMethod())
                .userId(orderEntity.getUser().getId())
                .build();
        orderResponse.setCreateDate(orderEntity.getCreateDate());
        orderResponse.setModifyDate(orderEntity.getModifyDate());
        return orderResponse;
    }
}
