package com.example.shopweb.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_product_image")
public class ProductImageEntity extends BaseObject {

  public static final int MAXIMUM_IMAGES_PER_PRODUCT = 5;

  @Column(name = "image_url", length = 300)
  private String imageUrl;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private ProductEntity product;

}