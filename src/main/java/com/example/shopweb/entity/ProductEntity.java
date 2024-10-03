package com.example.shopweb.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "tbl_product")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ProductEntity extends BaseObject {

  @Comment("Tên sản phẩm")
  @Column(nullable = false,length = 300)
  private String name;

  @Min(value = 0)
  @Column(nullable = false)
  private float price;

  @Column(length = 300)
  private String thumbnail;

  @Column(columnDefinition = "text")
  private String description;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private CategoryEntity category;

  @OneToMany(mappedBy = "product", orphanRemoval = true)
  private List<OrderDetailEntity> orderDetails;

  @OneToMany(mappedBy = "product", orphanRemoval = true)
  private List<ProductImageEntity> productImages;

}