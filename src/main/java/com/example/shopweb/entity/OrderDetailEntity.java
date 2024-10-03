package com.example.shopweb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_order_detail")
public class OrderDetailEntity extends BaseObject {

    @Column(nullable = false)
    @Min(value = 0)
    private float price;

    @Column(name = "number_of_products",nullable = false)
    @Min(value = 1)
    private int numberOfProducts;

    @Column(name = "total_money",nullable = false)
    private float totalMoney;

    @Column(length = 20)
    private String color;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

}