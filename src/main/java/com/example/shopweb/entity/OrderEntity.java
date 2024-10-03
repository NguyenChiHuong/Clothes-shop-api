package com.example.shopweb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "tbl_order")
public class OrderEntity extends BaseObject {

  public enum UserStatus{
    pending,
    processing,
    shipped,
    delivered,
    cancelled
  }

  @Column(length = 100)
  private String username;

  @Column(length = 100,nullable = false)
  private String email;

  @Column(name = "phone_number",length = 10,nullable = false)
  private String phoneNumber;

  @Column(length = 200,nullable = false)
  private String address;

  @Column(length = 100)
  private String note;

  @Column(name = "order_date")
  private Date orderDate;

  @Column(length = 20)
  @Comment("Trạng thái đơn hàng")
  @Enumerated(EnumType.STRING)
  private UserStatus status;

  @Min(value = 0)
  @Column(name = "total_money", nullable = false)
  private float totalMoney;

  @Column(name = "shipping_method", length = 100)
  private String shippingMethod;

  @Column(name = "shipping_address", length = 200)
  private String shippingAddress;

  @Column(name = "shipping_date")
  private LocalDate shippingDate;

  @Column(name = "tracking_number", length = 100)
  private String trackingNumber;

  @Column(name = "payment_method", length = 100)
  private String paymentMethod;

  @Column(nullable = false)
  private boolean active;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @OneToMany(mappedBy = "order", orphanRemoval = true)
  private List<OrderDetailEntity> orderDetails;

}