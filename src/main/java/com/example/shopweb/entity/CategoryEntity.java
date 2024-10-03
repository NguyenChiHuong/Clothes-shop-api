package com.example.shopweb.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "tbl_category")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CategoryEntity extends BaseObject {

  @Comment("Tên danh mục")
  @Column(nullable = false, length = 100)
  private String name;

  @JsonManagedReference
  @OneToMany(mappedBy = "category", orphanRemoval = true)
  private List<ProductEntity> products;

}
